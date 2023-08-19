package springboard;

import net.fabricmc.api.EnvType;
import net.fabricmc.devlaunchinjector.Main;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.launch.knot.Knot;
import net.fabricmc.loader.impl.launch.knot.KnotClient;
import springboard.api.SpringboardMeta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

public class Springboard {
    public static List<Path> suggestedMods;
    public static List<String> suggestedEntrypoints;
    //    public static URLClassLoader SPRINGBOARD_CLASS_LOADER;
//    public static List<SpringboardMeta> METAS = new ArrayList<>();
//    public static void createClassLoader(List<String> jarNames) throws IOException, URISyntaxException {
//        ArrayList<URL> jars = new ArrayList<>();
//        Path jarFolder = FabricLoader.getInstance().getGameDir().resolve(".springboard").resolve("jarCache");
//        jarFolder.toFile().mkdirs();
//        for (String jarName : jarNames) {
//            System.out.println(jarFolder.resolve(jarName).toFile().toURI().toURL().toString());
//            URL jarURL = new URL("jar:" + jarFolder.resolve(jarName).toFile().getAbsoluteFile().toURI().toURL().toString() + "!/");
//            URL meta = new URL(jarURL.toString()+"springboard.meta.json");
//            String metaStr = new String(meta.openStream().readAllBytes());
//            METAS.add(SpringboardMeta.read(metaStr));
//            System.out.println(metaStr);
//            jars.add(jarURL);
//        }
//        URLClassLoader cl = new URLClassLoader("springboard", jars.toArray(new URL[0]), Springboard.class.getClassLoader());
//        SPRINGBOARD_CLASS_LOADER = cl;
//    }

    public static void main(String[] args, List<Path> suggestedMods, List<String> suggestedEntrypoints, boolean client) throws NoSuchFieldException, IllegalAccessException {
        Springboard.suggestedMods = suggestedMods;
        Springboard.suggestedEntrypoints = suggestedEntrypoints;
        System.out.println(FabricLauncherBase.class.getClassLoader());
        Field launcher = FabricLauncherBase.class.getDeclaredField("properties");
        launcher.setAccessible(true);
        launcher.set(null, null);

        launcher = FabricLauncherBase.class.getDeclaredField("launcher");
        launcher.setAccessible(true);
        launcher.set(null, null);
        try {
//            System.setProperty("fabric.dli.config", "/Users/josephyap/Documents/GitHub/bleh/springboard/.gradle/loom-cache/launch.cfg");
//            System.setProperty("fabric.dli.env", "client");
//            System.setProperty("fabric.dli.main", "net.fabricmc.loader.impl.launch.knot.KnotClient");
            System.setProperty("springboard", "true");
//            System.setProperty("org.lwjgl.glfw.checkThread0", "false");

            ArrayList<String> args2 = new ArrayList<>();
            try {
                for (int i = 0; i < args.length; i += 2) {
                    String key = args[i];
                    String value = args[i + 1];
                    if ("--version".equals(key)) continue;
                    if ("--versionType".equals(key)) continue;
                    args2.add(key);
                    args2.add(value);
                }
            } catch (Throwable t) {
                args2.clear();
                args2.addAll(Arrays.asList(args));
            }

            System.out.println(Arrays.toString(args));
            Knot.launch(args2.toArray(new String[0]), client ? EnvType.CLIENT : EnvType.SERVER);
            //Main.main(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
