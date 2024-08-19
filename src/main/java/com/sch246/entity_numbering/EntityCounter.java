package com.sch246.entity_numbering;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EntityCounter {
    private static final String NAMESPACE = "entity_counter";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<EntityType<?>, Integer> entityCounts = new HashMap<>();

    private EntityCounter() {
        // 私有构造函数防止实例化
    }

    public static void loadData(MinecraftServer server) {
        Path savePath = getSaveFilePath(server);
        if (Files.exists(savePath)) {
            try (Reader reader = Files.newBufferedReader(savePath)) {
                JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
                deserialize(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            entityCounts.clear();
        }
    }

    public static void saveData(MinecraftServer server) {
        Path savePath = getSaveFilePath(server);
        try {
            Files.createDirectories(savePath.getParent());
            try (Writer writer = Files.newBufferedWriter(savePath)) {
                JsonObject jsonObject = serialize();
                GSON.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path getSaveFilePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("data").resolve(NAMESPACE + ".json");
    }

    private static JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<EntityType<?>, Integer> entry : entityCounts.entrySet()) {
            String key = Registries.ENTITY_TYPE.getId(entry.getKey()).toString();
            jsonObject.addProperty(key, entry.getValue());
        }
        return jsonObject;
    }

    private static void deserialize(JsonObject jsonObject) {
        entityCounts.clear();
        for (String key : jsonObject.keySet()) {
            EntityType<?> entityType = Registries.ENTITY_TYPE.get(Identifier.tryParse(key));
            int count = jsonObject.get(key).getAsInt();
            entityCounts.put(entityType, count);
        }
    }

    public static int getNextCount(EntityType<?> entityType) {
        int nextCount = entityCounts.getOrDefault(entityType, 0) + 1;
        entityCounts.put(entityType, nextCount);
        return nextCount;
    }

    public static int getCurrentCount(EntityType<?> entityType) {
        return entityCounts.getOrDefault(entityType, 0);
    }

    public static void setCount(EntityType<?> entityType, int count) {
        entityCounts.put(entityType, count);
    }
}
