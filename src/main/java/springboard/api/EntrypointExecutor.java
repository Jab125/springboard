package springboard.api;

import springboard.Springboard;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EntrypointExecutor {
//    public static <T> T[] getEntrypointObjects(String entrypoint, Class<T> clazz) {
//        ArrayList<Object> epInstances = new ArrayList<>();
//        try {
//            for (SpringboardMeta meta : Springboard.METAS) {
//                if (!meta.entrypoints().containsKey(entrypoint)) {
//                    continue;
//                }
//                for (String s : meta.entrypoints().get(entrypoint)) {
//                    epInstances.add(Springboard.SPRINGBOARD_CLASS_LOADER.loadClass(s).newInstance());
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        T[] array = (T[]) Array.newInstance(clazz, Math.min(255, epInstances.size()));
//        return epInstances.toArray(array);
//    }
}
