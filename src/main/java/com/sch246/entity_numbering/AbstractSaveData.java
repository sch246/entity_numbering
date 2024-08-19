package com.sch246.entity_numbering;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractSaveData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String namespace;

    protected AbstractSaveData(String namespace) {
        this.namespace = namespace;
    }

    public void loadData(MinecraftServer server) {
        Path savePath = getSaveFilePath(server);
        if (Files.exists(savePath)) {
            try (Reader reader = Files.newBufferedReader(savePath)) {
                JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
                deserialize(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setDefaultData();
        }
    }

    public void saveData(MinecraftServer server) {
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

    private Path getSaveFilePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("data").resolve(namespace + ".json");
    }

    protected abstract void setDefaultData();
    protected abstract JsonObject serialize();
    protected abstract void deserialize(JsonObject jsonObject);
}
