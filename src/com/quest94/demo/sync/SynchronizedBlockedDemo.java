package com.quest94.demo.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 目标：演示 synchronized 不可中断 和 ReentrantLock 中断机制
 * 1.定义一个 Runnable
 * 2.在 Runnable 定义同步代码块
 * 3.首先开启一个线程来执行同步代码块，保证不退出同步代码块
 * 4.然后开启一个线程来执行同步代码块（阻塞状态）
 * 5.中断第二个线程
 */
public class SynchronizedBlockedDemo {


    public static void main(String[] args) throws InterruptedException {
        // 1.定义一个Runnable
        // 2.在Runnable定义同步代码块
        Runnable run = new SynchronizedBlocked();
//        Runnable run = new ReentrantLockInterruptibly();
        // 3.首先开启一个线程来执行同步代码块
        Thread t1 = new Thread(run);
        t1.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        // 4.然后开启一个线程来执行同步代码块(阻塞状态)
        Thread t2 = new Thread(run);
        t2.start();
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                // 5.中断第二个线程
                System.out.println("before stop thread");
                t2.interrupt();
                System.out.println("after stop thread");
            }
            System.out.println(i + "： " + t1.getName() + " state: " + t1.getState());
            System.out.println(i + "： " + t2.getName() + " state: " + t2.getState());
            TimeUnit.MILLISECONDS.sleep(1000);
        }

        System.out.println("method end");
        t1.interrupt();
    }

    static class SynchronizedBlocked implements Runnable {

        private static final Object LOCK = new Object();

        public void run() {
            // 2.在Runnable定义同步代码块
            synchronized (LOCK) {
                String name = Thread.currentThread().getName();
                System.out.println(name + " enter synchronized code");
                // 保证不退出同步代码块
                try {
                    TimeUnit.MINUTES.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + " exit synchronized code");
            }
        }

    }

    static class ReentrantLockInterruptibly implements Runnable {

        private static final ReentrantLock LOCK = new ReentrantLock();

        public void run() {
            // 2.在Runnable定义同步代码块
            try {
                LOCK.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + " end on InterruptedException");
                return;
            }
            try {
                System.out.println(Thread.currentThread().getName() + " enter synchronized code");
                try {
                    // 保证不退出同步代码块
                    TimeUnit.MINUTES.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " exit synchronized code");
            } finally {
                LOCK.unlock();
            }
        }

    }
}

