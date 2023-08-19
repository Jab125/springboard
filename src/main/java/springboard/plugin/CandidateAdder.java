package springboard.plugin;

import springboard.tweak.classloader.ClassTransformer;

import java.net.URL;
import java.nio.file.Path;

public interface CandidateAdder {
    void suggest(Path path);

    void cancelCompletely();

    // will be added to classpath.
    void suggestPreinitClasspathAddition(URL path);

    void suggestEarlyEntrypoint(String clazz);

    void suggestExclusion(String prefix);

    void suggestTransformer(ClassTransformer transformer);
}