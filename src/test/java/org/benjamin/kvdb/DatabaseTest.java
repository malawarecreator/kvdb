package org.benjamin.kvdb;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DatabaseTest {
    @Test
    void databaseSave() {
        Database<String, Integer> db = new Database<>("test db", "C:/Users/benjamin/test.json");
        db.save(new DataBlock<>("age", 13));
        db.save(new DataBlock<>("grade", 7));

    }

}