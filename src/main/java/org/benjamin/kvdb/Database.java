package org.benjamin.kvdb;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Database<K, V> {
    private final ArrayList<DataBlock<K, V>> blocks;
    public String id;
    public String defaultPath;
    private final Class<V> valueClass;

    public Database(String id, String defaultPath, boolean deserialize, Class<V> valueClass) throws IOException {
        this.blocks = new ArrayList<>();
        this.id = id;
        this.defaultPath = defaultPath;
        this.valueClass = valueClass;

        if (deserialize) {
            try (FileReader reader = new FileReader(defaultPath)) {
                Gson gson = new Gson();
                Type type = TypeToken.getParameterized(Map.class, String.class, valueClass).getType();
                Map<String, V> loadedMap = gson.fromJson(reader, type);

                if (loadedMap != null) {
                    for (Map.Entry<String, V> entry : loadedMap.entrySet()) {
                        this.blocks.add(new DataBlock<>((K) entry.getKey(), entry.getValue()));
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found at " + defaultPath + ". Starting with empty database.");
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing JSON from " + defaultPath + ". File might be corrupted or empty. Starting with empty database.");
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("An unexpected error occurred during deserialization.");
                e.printStackTrace();
            }
        } else {
            try (FileWriter writer = new FileWriter(defaultPath, false)) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save(DataBlock<K, V> block) {
        boolean found = false;
        for (int i = 0; i < this.blocks.size(); i++) {

            if (this.blocks.get(i).getKey().equals(block.getKey())) {
                this.blocks.get(i).setValue(block.getValue());
                found = true;
                break;
            }
        }
        if (!found) {
            this.blocks.add(block);
        }
        this.sync();
    }

    public void saveMultiple(List<DataBlock<K, V>> blocksToSave) {
        for (DataBlock<K, V> block : blocksToSave) {
            this.save(block);
        }
    }

    public V get(K key) {
        for (DataBlock<K, V> block : this.blocks) {

            if (block.getKey().equals(key)) {
                return block.getValue();
            }
        }
        return null;
    }

    public boolean delete(K key) {
        boolean removed = false;
        for (int i = 0; i < this.blocks.size(); i++) {

            if (this.blocks.get(i).getKey().equals(key)) {
                this.blocks.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) {
            this.sync();
        }
        return removed;
    }

    public void sync() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, V> mapForSerialization = new HashMap<>();

        for (DataBlock<K, V> block : this.blocks) {

            mapForSerialization.put(String.valueOf(block.getKey()), block.getValue());
        }

        String json = gson.toJson(mapForSerialization);

        try (FileWriter writer = new FileWriter(this.defaultPath)) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + this.defaultPath);
            e.printStackTrace();
        }
    }
}