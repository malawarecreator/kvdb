package org.benjamin.kvdb;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DatabaseTest {
    @Test
    void databaseSave() throws IOException {
        Database<String, Integer> db = new Database<>("stuff", "C:/Users/benjamin/test.json", false, Integer.class);
        db.save(new DataBlock<>("stuff", 1));
        
    }

}