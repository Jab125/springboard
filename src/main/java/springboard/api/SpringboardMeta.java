package springboard.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

public interface SpringboardMeta {
    String id();
    String name();
    String description();
    String[] authors();
    Map<String, String[]> entrypoints();
    /* by unloadable it means can be unloaded during runtime. */
    boolean unloadable(); // [unloadable and runtimeLoadable]s can be effectively reloaded during runtime
    boolean runtimeLoadable();


    public static SpringboardMeta read(String str) {
        JsonObject jsonObject = new Gson().fromJson(str, JsonObject.class);
        return new SpringboardMeta() {
            @Override
            public String id() {
                return jsonObject.getAsJsonPrimitive("id").getAsString();
            }

            @Override
            public String name() {
                return jsonObject.getAsJsonPrimitive("name").getAsString();
            }

            @Override
            public String description() {
                return jsonObject.getAsJsonPrimitive("description").getAsString();
            }

            @Override
            public String[] authors() {
                return jsonObject.getAsJsonArray("authors").asList().stream().map(JsonElement::getAsString).toArray(String[]::new);
            }

            @Override
            public Map<String, String[]> entrypoints() {
                HashMap<String, String[]> m = new HashMap<>();
                var eps = jsonObject.getAsJsonObject("entrypoints");
                for (String s : eps.keySet()) {
                    m.put(s, eps.get(s).getAsJsonArray().asList().stream().map(JsonElement::getAsString).toArray(String[]::new));
                }
                return Map.copyOf(m);
            }

            @Override
            public boolean unloadable() {
                return jsonObject.getAsJsonPrimitive("unloadable").getAsBoolean();
            }

            @Override
            public boolean runtimeLoadable() {
                return jsonObject.getAsJsonPrimitive("runtimeLoadable").getAsBoolean();
            }
        };
    }
}
