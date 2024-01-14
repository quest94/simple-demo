package com.quest94.demo.memory.allocation;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class HeapAllocationTest {

    byte[] bytes = new byte[1024 * 100];// 100KB

    public static void main(String[] args) throws InterruptedException {
        ArrayList<HeapAllocationTest> heapTests = new ArrayList<>();
        while (true) {
            heapTests.add(new HeapAllocationTest());
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }

}