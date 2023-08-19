package springboard.plugin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import springboard.init.Init;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

public class SpringboardPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

        Init.extracted("mixin plugin");
//        List<JarFile> jars = new ArrayList<>();
//        searchForJars(jars);
//        for (JarFile jar : jars) {
//
//        }
//        try {
//            Springboard.createClassLoader(List.of("springboard-1.0.0-dev.jar"));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    private void searchForJars(List<JarFile> jarFiles) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
