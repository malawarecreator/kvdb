package org.benjamin.kvdb;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

public class Database<K, V> {
    private final ArrayList<DataBlock<K, V>> blocks;
    public String id;
    public String defaultPath;
    public Database(String id, String defaultPath) {
        try {
            PrintWriter writer = new PrintWriter(new File(defaultPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.blocks = new ArrayList<>();
        this.id = id;
        this.defaultPath = defaultPath;
    }

    public void save(DataBlock<K, V> block) {
        for (DataBlock<K, V> kvDataBlock : this.blocks) {
            if (kvDataBlock.getKey() == block.getKey()) {
                return;
            }
        }
        this.blocks.add(block);
        this.sync();


    }

    public void saveMultiple(List<DataBlock<K, V>> blocks) {
        for (DataBlock<K, V> block : blocks) {
            this.save(block);
        }
    }




    public V get(K key) {
        for (DataBlock<K, V> block : this.blocks) {
            if (block.getKey() == key) {
                return block.getValue();
            }
        }
        return null;
    }

    public int delete(K key) throws IOException {
        for (int i = 0; i < this.blocks.size(); i++) {
            if (this.blocks.get(i).getKey() == key) {

                this.blocks.remove(i);
                this.sync();
                return 0;
            }
        }
        return 1;
    }


    public void sync() {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        for (DataBlock<K, V> block : this.blocks) {
            map.put(block.getKey().toString(), block.getValue().toString());
        }

        String json = gson.toJson(map);

        try (FileWriter writer = new FileWriter(this.defaultPath)){
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
