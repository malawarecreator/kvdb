package org.benjamin.kvdb;

import java.util.ArrayList;
import java.util.List;

public class Database<K, V> {
    private ArrayList<DataBlock<K, V>> blocks;
    public String id;

    public Database(String id) {
        this.blocks = new ArrayList<>();
        this.id = id;
    }

    public void save(DataBlock<K, V> block) {
        this.blocks.add(block);
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

    public int delete(K key) {
        for (int i = 0; i < this.blocks.size(); i++) {
            if (this.blocks.get(i).getKey() == key) {
                this.blocks.remove(i);
                return 0;
            }
        }
        return 1;
    }




}
