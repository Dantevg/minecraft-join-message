package com.sam.joinmessage;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Config {
    public static File configFile;

    public static class Messages {
        public String welcome;
        public List<String> random;
    }

    public static void createConfig() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("default_config.json");
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            Messages defaultMessages = gson.fromJson(streamReader, Messages.class);
            streamReader.close();
            Writer writer = new FileWriter(configFile);
            gson.toJson(defaultMessages, writer);
            writer.close();
        } catch (IOException e) {
            JoinMessage.LOGGER.error("Couldn't save Join Message config.");
        }
    }

    public static Messages loadConfig() {
        configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), JoinMessage.MOD_ID + ".json");
        try {
            if (!configFile.exists()) {
                createConfig();
            }

            Reader reader = new FileReader(configFile);
            Gson gson = new Gson();
            Messages messages = gson.fromJson(reader, Messages.class);
            reader.close();
            return messages;
        } catch (IOException e) {
            JoinMessage.LOGGER.error("Couldn't load Join Message config.");
        }
        return new Messages();
    }
}
