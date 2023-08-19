package springboard;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerContext.getContext().getLogger("springboard");

	@Override
	public void onInitialize() {
		if ("true".equals(System.getProperty("springboard"))) return;
		LOGGER.fatal("Springboard failed to load.");
	}
}