package org.benjamin.kvdb;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DatabaseTest {
    @Test
    void databaseSave() {
        Database<String, Integer> db = new Database<>("test db");
        db.save(new DataBlock<>("age", 13));
        System.out.println(db.get("age"));
    }
    @Test
    void databaseSaveMultiple() {
        Database<String, Integer> db = new Database<>("test db");
        List<DataBlock<String, Integer>> blocks = List.of(new DataBlock<>("hawk", 5));
        db.saveMultiple(blocks);

        System.out.println(db.get("age"));
    }
}