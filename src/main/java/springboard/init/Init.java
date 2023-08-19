package springboard.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import springboard.api.SpringboardCandidateEntrypoint;
import springboard.plugin.CandidateAdder;
import springboard.plugin.SpringboardPlugin;
import springboard.tweak.classloader.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Init {
    public static void extracted(String from) {
        if ("true".equals(System.getProperty("springboard"))) return;
        System.out.println("Loading Springboard via " + from);
        boolean client = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
        final ArrayList<Path> suggestedMods = new ArrayList<>();
        final ArrayList<URL> suggestedClasspathAddition = new ArrayList<>();
        final ArrayList<String> suggestedEarlyEntrypoint = new ArrayList<>();
        final ArrayList<String> exclusions = new ArrayList<>();
        final ArrayList<ClassTransformer> transformers = new ArrayList<>();
        AtomicBoolean cancelled = new AtomicBoolean(false);
        List<SpringboardCandidateEntrypoint> entrypoints = new ArrayList<>(FabricLoader.getInstance().getEntrypoints("springboard:mod-suggestor", SpringboardCandidateEntrypoint.class));
        for (ModContainer allMod : FabricLoader.getInstance().getAllMods()) {
            if (allMod.getMetadata().containsCustomValue("springboard:mod-suggestor")) {
                try {
                    entrypoints.add((SpringboardCandidateEntrypoint) Class.forName(allMod.getMetadata().getCustomValue("springboard:mod-suggestor").getAsString()).newInstance());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        for (SpringboardCandidateEntrypoint entrypoint : entrypoints) {
            entrypoint.suggestMods(new CandidateAdder() {
                @Override
                public void suggest(Path path) {
                    suggestedMods.add(path);
                }

                @Override
                public void cancelCompletely() {
                    cancelled.set(true);
                }

                @Override
                public void suggestPreinitClasspathAddition(URL path) {
                    suggestedClasspathAddition.add(path);
                }

                @Override
                public void suggestEarlyEntrypoint(String clazz) {
                    suggestedEarlyEntrypoint.add(clazz);
                }

                @Override
                public void suggestExclusion(String prefix) {
                    exclusions.add(prefix);
                }

                @Override
                public void suggestTransformer(ClassTransformer transformer) {
                    transformers.add(transformer);
                }
            });
            if (cancelled.get()) return;
        }
        String[] launchArguments = FabricLoader.getInstance().getLaunchArguments(false);
//        try {
//         //   Files.copy(FabricLoader.getInstance().getGameDir().resolve(".springboard/modmenu-7.2.1.jar"), FabricLoader.getInstance().getGameDir().resolve("mods/modmenu-7.2.1.jar"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        TweakingClassLoader tweakingClassLoader = new TweakingClassLoader(getURLs(suggestedClasspathAddition), exclusions);
        tweakingClassLoader.addTransformer(new FabricLoaderImplTransformer());
        tweakingClassLoader.addTransformer(new CustomCandiateTransformer());
        tweakingClassLoader.addTransformer(new ModCandidateTransformer());
        for (ClassTransformer transformer : transformers) {
            tweakingClassLoader.addTransformer(transformer);
        }
        Thread.currentThread().setContextClassLoader(tweakingClassLoader);
        Thread.currentThread().setUncaughtExceptionHandler((a, b) -> {
            if (!"main thread closed.".equals(b.getMessage())) b.printStackTrace();
            //System.exit(-1);
        });
        try {
            //   Thread thread = new Thread(() -> {
            Method main = null;
            try {
                //System.setProperty("fabric.dli.config", "/Users/josephyap/Documents/GitHub/bleh/springboard/.gradle/loom-cache/launch.cfg"); System.setProperty("fabric.dli.env", "client"); System.setProperty("fabric.dli.main", "net.fabricmc.loader.impl.launch.knot.KnotClient");
                main = Class.forName("springboard.Springboard", true, tweakingClassLoader).getMethod("main", String[].class, List.class, List.class, boolean.class);
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                main.invoke(null, (Object) launchArguments, suggestedMods, suggestedEarlyEntrypoint, client);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            // net.fabricmc.devlaunchinjector.Main.main(new String[0]);
            //    });
            //    thread.start();
        } catch (Throwable e) {
            throwsUnchecked(new Throwable(e));
            //System.exit(0);
        }
//        while(true) {
//        }
        Thread.currentThread().setUncaughtExceptionHandler((a, b) -> {
            //if ((!"main thread closed.".equals(b.getMessage())) || (b.getCause() != null && !"main thread closed.".equals(b.getCause().getMessage()))) b.printStackTrace();
        });
        throwsUnchecked(new Throwable("main thread closed."));
       // System.exit(0);
    }

    private static URL[] getURLs(ArrayList<URL> suggestedClasspathAddition) {
        boolean dev = FabricLoader.getInstance().isDevelopmentEnvironment();
        String cp = System.getProperty("java.class.path");
        String[] elements = cp.split(File.pathSeparator);
        if (elements.length == 0) {
            elements = new String[]{""};
        }
        ArrayList<URL> urls = new ArrayList<URL>();
        for (String element : elements) {
            try {
                URL url = new File(element).toURI().toURL();
                urls.add(url);
            } catch (MalformedURLException ignore) {
                // malformed file string or class path element does not exist
            }
        }
        urls.addAll(suggestedClasspathAddition);
        if (!dev) urls.add(SpringboardPlugin.class.getProtectionDomain().getCodeSource().getLocation());
        return urls.toArray(new URL[0]);
    }

    /**
     * Remember, Generics are erased in Java. So this basically throws an Exception. The real
     * Type of T is lost during the compilation
     */
    public static <T extends Throwable> void throwsUnchecked(Throwable toThrow) throws T {
        // Since the type is erased, this cast actually does nothing!!!
        // we can throw any exception
        throw (T) toThrow;
    }

    /**
     * Remember, Generics are erased in Java. So this basically throws an Exception. The real
     * Type of T is lost during the compilation
     */
    public static <T extends Throwable, U> U throwsUncheckedReturn(Throwable toThrow) throws T {
        // Since the type is erased, this cast actually does nothing!!!
        // we can throw any exception
        throw (T) toThrow;
    }
}
