package springboard.tweak.classloader;


import springboard.Springboard;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TweakingClassLoader extends URLClassLoader {

    private final List<URL> classpath;
    private final Map<String, Class<?>> cachedClasses;
    private final List<String> exclusions;
    private final Map<String, byte[]> cachedResources;
    private final List<ClassTransformer> classTransformers = new ArrayList<>();

    public TweakingClassLoader(URL[] classpath, List<String> exclusions2) {
        super(classpath, null);//getSystemClassLoader());
        this.classpath = new ArrayList<>(Arrays.asList(classpath));
        this.cachedClasses = new HashMap<>();
        exclusions = new ArrayList<>();
        exclusions.add("java.");
        exclusions.add("springboard.plugin.");
        exclusions.add("springboard.tweak.");
        exclusions.add("javax.");
        exclusions.add("sun.misc.");
        exclusions.add("sun.reflect.");
        exclusions.add("jdk.");
        exclusions.add("com.sun.");
        exclusions.addAll(exclusions2);
        cachedResources = new ConcurrentHashMap<>();
        loadTransformersFromServices();
        //classTransformers.add(new MainModifierTransformer());
    }

    private void loadTransformersFromServices() {
        for (ClassTransformer classTransformer : ServiceLoader.load(ClassTransformer.class)) {
            addTransformer(classTransformer);
        }
    }

    public void addTransformer(ClassTransformer transformer) {
        classTransformers.add(transformer);
    }

    private String toInternalName(String className) {
        return className.replaceAll("\\.", "/");
    }

    private String toApiName(String className) {
        return className.replaceAll("/", ".");
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (exclusions.stream().anyMatch(name::startsWith)) {
            return Class.forName(name, true, getSystemClassLoader());
        }

        try {
            if (cachedClasses.containsKey(name)) return cachedClasses.get(name);

            final int lastDot = name.lastIndexOf('.');
            final String packageName = lastDot == -1 ? "" : name.substring(0, lastDot);
            //URLConnection urlConnection = findUrlConnection(fileName);

            Package pkg = getPackage(packageName);
            if (pkg == null) {
                pkg = definePackage(packageName, null, null, null, null, null, null, null);
            } else if (pkg.isSealed()) {
                System.err.println("WHOOP, WHOOP, WHOOP");
            }
            byte[] clazzBytes = findClassBytes(name);
            clazzBytes = transformClass(name, clazzBytes);
            URL classResource;
            {
                String classFileName = toInternalName(name) + ".class";
                URL g = findResource(classFileName);
                String h = g.toString();
                h = h.replaceAll("jar:", "").split("!")[0];
                classResource = new URL(h);
            }
            ProtectionDomain domain = new ProtectionDomain(new CodeSource(classResource, new CodeSigner[0]), new Permissions());
            Class<?> clazz = defineClass(name, clazzBytes, 0, clazzBytes.length, domain);
      //      System.out.println(clazz.getProtectionDomain().getCodeSource());
            cachedClasses.put(name, clazz);
            return clazz;
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to define " + name, t);
        }

        //return super.findClass(name);
    }

    private byte[] transformClass(String name, byte[] clazzBytes) {
        for (ClassTransformer classTransformer : classTransformers) {
            if (classTransformer.shouldTransform(name)) {
                clazzBytes = classTransformer.transformClass(name, clazzBytes);
            }
        }
        return clazzBytes;
    }

    public List<URL> getClasspath() {
        return classpath;
    }

    private URLConnection findUrlConnection(final String name) {
        final URL resource = findResource(name);
        if (resource == null) return null;
        try {
            return resource.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // add a mod loader?
    @Override
    public void addURL(URL url) {
        classpath.add(url);
        super.addURL(url);
    }

    @Override
    protected Class<?> findClass(String moduleName, String name) {
        //return super.findClass(moduleName, name);
        try {
            return findClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] findClassBytes(String className) {
     //   System.out.println("Loading " + className);
        String classFileName = toInternalName(className) + ".class";
        if (cachedResources.containsKey(classFileName)) return cachedResources.get(classFileName);
        URL classResource = findResource(classFileName);
        try {
            byte[] classBytes = classResource.openStream().readAllBytes();
            cachedResources.put(classFileName, classBytes);
            return classBytes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load class: " + className, e);
        }
    }
}
