package springboard.tweak.classloader;

public interface ClassTransformer {
    boolean shouldTransform(String name);
    byte[] transformClass(String className, byte[] in);
}
