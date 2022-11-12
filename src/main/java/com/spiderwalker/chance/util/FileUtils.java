package com.spiderwalker.chance.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.spiderwalker.chance.exception.RangeError;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());
    public static File readFile() {
        File file = null;
        try {
            ClassLoader classLoader = FileUtils.class.getClassLoader();
            String fileName = "data.json";
            file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,e.getMessage());
        }

        return file;
    }

    public static Map<String, Object> readJson() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            // create Gson instance
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(readFile().getAbsolutePath()));

            // convert JSON file to map
            jsonMap = (Map<String, Object>) gson.fromJson(reader, Map.class);
            reader.close();

        } catch (JsonParseException | IOException ex) {
            ex.printStackTrace();
        }
        return jsonMap;
    }
}
