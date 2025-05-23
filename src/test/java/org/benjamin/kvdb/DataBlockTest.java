package org.benjamin.kvdb;

import org.junit.jupiter.api.Test;

class DataBlockTest {
    @Test
    void testConstructor() {
        DataBlock<String, Integer> block = new DataBlock<>("age", 13);
        System.out.println(block.getKey() + " " + block.getValue());
    }
}