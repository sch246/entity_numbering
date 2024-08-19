package com.sch246.entity_numbering;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EntityNumberingConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("entity_numbering.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean enableNumbering = true;
    public boolean enableDeathMessages = true;
    public boolean boardcastNeedName = true;
    public int boardcastDistance = 128;
    public String nameSeparator = " #";

    public static EntityNumberingConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                return GSON.fromJson(Files.newBufferedReader(CONFIG_PATH), EntityNumberingConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new EntityNumberingConfig();
    }

    public void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
