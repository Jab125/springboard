package springboard.plugin;

import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;
import springboard.init.Init;

public class SpringboardLanguageAdapter implements LanguageAdapter {
    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        return Init.throwsUncheckedReturn(new Throwable());
    }

    static {
        Init.extracted("language adapter");
    }
}
