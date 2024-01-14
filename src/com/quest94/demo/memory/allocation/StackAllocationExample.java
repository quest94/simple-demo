package com.quest94.demo.memory.allocation;

import java.util.function.Function;

public class StackAllocationExample {

    public static final String name = "xxxxx";

    public static void main(String[] args) {
        // -Xmx9m -Xms9m -XX:+PrintGC -XX:+PrintHeapAtGC
//        Function<Integer, Integer> function = StackAllocationExample::add;
//        forExample(function);
        Function<Integer, Integer> function1 = StackAllocationExample::add1;
        forExample(function1);
//        Function<Integer, Integer> function2 = i -> StackAllocationExample.add2(i).add() + 1;
//        forExample(function2);
    }

    private static void forExample(Function<Integer, Integer> function) {
        long nanoTime = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < 1000000000; i++) {
            int added = function.apply(i);
            sum += added;
        }
        System.out.println(sum);
        System.out.println((System.nanoTime() - nanoTime) / 100000.0);
    }

    private static int add(int i) {
        ClassA classA = new ClassA();
        classA.setId(i);
        classA.setName(name);
        return name.length() + i + 1;
    }

    private static int add1(int i) {
        ClassA classA = new ClassA();
        classA.setId(i);
        classA.setName(name);
        return classA.add() + 1;
    }

    private static ClassA add2(int i) {
        ClassA classA = new ClassA();
        classA.setId(i);
        classA.setName(name);
        return classA;
    }

    public static class ClassA {
        int id;
        String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int add() {
            return name.length() + id;
        }

    }

}
