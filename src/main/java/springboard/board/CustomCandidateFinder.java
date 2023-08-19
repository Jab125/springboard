package springboard.board;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.discovery.ModCandidateFinder2;
import springboard.Springboard;
import springboard.api.SpringboardCandidateEntrypoint;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CustomCandidateFinder implements ModCandidateFinder2 {
    @Override
    public void findCandidates(ModCandidateConsumer out) {
        List<Path> p = new ArrayList<>();
//        CandidateAdder adder = new CandidateAdder() {
//            @Override
//            public void suggest(Path path) {
//                p.add(path);
//            }
//
//            @Override
//            public void cancelCompletely() {
//                throw new RuntimeException();
//            }
//
//            @Override
//            public void suggestPreinitClasspathAddition(URL path) {
//                throw new RuntimeException();
//            }
//
//            @Override
//            public void suggestEarlyEntrypoint(String clazz) {
//                throw new RuntimeException();
//            }
//        };
//        for (String suggestedEntrypoint : Springboard.suggestedEntrypoints) {
//            try {
//                SpringboardCandidateEntrypoint springboardCandidateEntrypoint = (SpringboardCandidateEntrypoint) Class.forName(suggestedEntrypoint).newInstance();
//                springboardCandidateEntrypoint.suggestMods(adder);
//            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
        for (Path path : p) {
            out.accept(path, FabricLoader.getInstance().isDevelopmentEnvironment());
        }
        out.accept(Springboard.suggestedMods, FabricLoader.getInstance().isDevelopmentEnvironment());
//        java.nio.file.Path gameDir = FabricLoaderImpl.INSTANCE.getGameDir();
//        gameDir.resolve(".springboard").toFile().mkdirs();
//        if (!gameDir.resolve(".springboard/modmenu.jar").toFile().exists()) {
//            try {
//                Files.write(gameDir.resolve(".springboard/modmenu.jar"), new URL("https://cdn.modrinth.com/data/mOgUt4GM/versions/eTCL1uh8/modmenu-7.2.1.jar").openStream().readAllBytes());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        out.accept(gameDir.resolve(".springboard/modmenu.jar"), FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment());
    }
}
