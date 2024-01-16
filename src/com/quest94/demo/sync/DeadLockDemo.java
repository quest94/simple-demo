package com.quest94.demo.sync;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DeadLockDemo {

    public static void main(String[] args) {
        /* 问题描述：有五位哲学家，围坐在圆桌旁；
         * 吃饭是要用两根筷子，桌子上共有5根筷子，每位哲学家左右手边各一根筷子；
         * 如果筷子被身边的人拿着，自己就等待。
         */
//        philosopherDinnerTest1();
//        philosopherDinnerTest2();
    }

    public static void philosopherDinnerDeadLock() {
        // 创建5根筷子
        Chopstick c1 = new Chopstick("筷子1");
        Chopstick c2 = new Chopstick("筷子2");
        Chopstick c3 = new Chopstick("筷子3");
        Chopstick c4 = new Chopstick("筷子4");
        Chopstick c5 = new Chopstick("筷子5");
        // 创建5个哲学家线程
        Arrays.asList(new Philosopher("苏格拉底", c1, c2),
                        new Philosopher("柏拉图", c2, c3),
                        new Philosopher("亚里士多德", c3, c4),
                        new Philosopher("赫拉克利特", c4, c5),
                        new Philosopher("阿基米德", c5, c1))
                .forEach(Thread::start);
    }

    public static void philosopherDinnerTilt() {
        // 创建5根筷子
        Chopstick c1 = new Chopstick("筷子1");
        Chopstick c2 = new Chopstick("筷子2");
        Chopstick c3 = new Chopstick("筷子3");
        Chopstick c4 = new Chopstick("筷子4");
        Chopstick c5 = new Chopstick("筷子5");
        // 创建5个哲学家线程
        Arrays.asList(new Philosopher("苏格拉底", c1, c2),
                        new Philosopher("柏拉图", c2, c3),
                        new Philosopher("亚里士多德", c3, c4),
                        new Philosopher("赫拉克利特", c4, c5),
                        // 只改这里顺序
                        new Philosopher("阿基米德", c1, c5))
                .forEach(Thread::start);
    }

    /**
     * 筷子类
     */
    public static class Chopstick {

        public Chopstick(String name) {
            this.name = name;
        }

        /**
         * 筷子名称
         */
        private String name;
    }

    /**
     * 哲学家类
     */
    public static class Philosopher extends Thread {

        private int count = 0;

        /**
         * 左手边筷子名称
         */
        private final Chopstick left;
        /**
         * 右手边筷子名称
         */
        private final Chopstick right;

        public Philosopher(String name, Chopstick left, Chopstick right) {
            super(name);
            this.left = left;
            this.right = right;
        }

        @Override
        public void run() {
//            System.out.println(getName() + " run");
            while (true) {
//                System.out.println(getName() + " waiting left-" + left.name + " waiting right-" + right.name);
                // 尝试获取左手边的筷子
                synchronized (left) {
//                    System.out.println(getName() + " obtained left-" + left.name + " waiting right-" + right.name);
                    // 尝试后去右手边的筷子
                    synchronized (right) {
//                        System.out.println(getName() + " obtained left-" + left.name + " obtained right-" + right.name);
                        // 吃饭
                        eat();
                    }
                }
            }
        }

        /**
         * 吃饭
         */
        private void eat() {
            System.out.println(getName() + " " + count++ + " eating ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}


