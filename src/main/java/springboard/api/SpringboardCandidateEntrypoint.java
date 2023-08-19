package springboard.api;

import springboard.board.CustomCandidateFinder;
import springboard.plugin.CandidateAdder;

@FunctionalInterface
public interface SpringboardCandidateEntrypoint {
    void suggestMods(CandidateAdder a);
}
