package springboard.impl;

import net.fabricmc.loader.api.FabricLoader;
import springboard.api.SpringboardCandidateEntrypoint;
import springboard.plugin.CandidateAdder;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ModMenuSuggester implements SpringboardCandidateEntrypoint {
    @Override
    public void suggestMods(CandidateAdder a) {
//        java.nio.file.Path gameDir = FabricLoader.getInstance().getGameDir();
//        gameDir.resolve(".springboard").toFile().mkdirs();
//        if (!gameDir.resolve(".springboard/modmenu.jar").toFile().exists()) {
//            try {
//                Files.write(gameDir.resolve(".springboard/modmenu.jar"), new URL("https://cdn.modrinth.com/data/mOgUt4GM/versions/eTCL1uh8/modmenu-7.2.1.jar").openStream().readAllBytes());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        a.suggest(gameDir.resolve(".springboard/modmenu.jar"));
    }
}
