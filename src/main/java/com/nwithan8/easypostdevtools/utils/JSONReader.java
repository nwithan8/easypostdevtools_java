package com.nwithan8.easypostdevtools.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONReader {

    public static Path getFilePathFromResources(String filename) throws URISyntaxException, IOException {
        URL resource = JSONReader.class.getClassLoader().getResource(filename);
        return Paths.get(resource.toURI());
    }

    private static ArrayList<Map<String, Object>> readJsonFileJson(String path) {
        try {
            Path filePath = getFilePathFromResources(path);
            Reader reader = Files.newBufferedReader(filePath);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
            }.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return null;
        }
    }

    private static ArrayList<Object> readJsonFileArray(String path) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Object>>() {
            }.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Map<String, Object>> getRandomMapsFromJsonFile(String path, int amount, boolean allowDuplicates) {
        ArrayList<Map<String, Object>> data = readJsonFileJson(path);
        List<Map<String, Object>> maps = new ArrayList<>();
        Random.getRandomItemsFromList(data, amount, allowDuplicates).forEach(item -> maps.add((Map<String, Object>) item));
        return maps;
    }

    public static List<Object> getRandomItemsFromJsonFile(String path, int amount, boolean allowDuplicates) {
        ArrayList<Object> data = readJsonFileArray(path);
        return Random.getRandomItemsFromList(data, amount, allowDuplicates);
    }
}
