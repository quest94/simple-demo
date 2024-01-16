package com.quest94.demo.sync;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
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
        RunnableHold runnableHold = new SynchronizedBlocked();
//        RunnableHold runnableHold = new ReentrantLockInterruptibly();
        // 3.首先开启一个线程来执行同步代码块
        // 4.然后再开启多个线程来执行同步代码块(阻塞状态)
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnableHold.getRunnable(), "t" + (i + 1));
            threads[i].start();
            // 睡500毫秒，依次进入等待锁队列
            TimeUnit.MILLISECONDS.sleep(500);
        }
        for (int i = 0; i < 15; i++) {
            if (i == 5) {
                Thread thread = threads[1];
                System.out.println(thread.getName() + ": before interrupt thread");
                // 5.中断第二个线程
                // synchronized-等待锁的线程处于BLOCKED状态，中断无效
                thread.interrupt();
                System.out.println(thread.getName() + ": after interrupt thread");
            }
            if (i == 10) {
                // 6.放开第一个线程
                Thread thread = threads[0];
                System.out.println(thread.getName() + ": before unpark thread");
                runnableHold.unpark(thread);
                System.out.println(thread.getName() + ": after unpark thread");
            }
            for (Thread thread : threads) {
                System.out.println(i + "： " + thread.getName() + " state: " + thread.getState());
            }
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("--------------------------");
        }

        System.out.println("method end");

        Arrays.stream(threads).forEach(runnableHold::unpark);
    }

    interface RunnableHold {

        Runnable getRunnable();

        void park(String name);

        void unpark(Thread thread);
    }


    static class SynchronizedBlocked implements RunnableHold {

        private static final Object LOCK = new Object();
        private final Runnable runnable = () -> {
            synchronized (LOCK) {
                // 2.在Runnable定义同步代码块
                String name = Thread.currentThread().getName();
                System.out.println(name + " enter synchronized code");
                // 保证不退出同步代码块
                park(name);
                System.out.println(name + " exit synchronized code");
            }
        };


        @Override
        public Runnable getRunnable() {
            return runnable;
        }

        @Override
        public void park(String name) {
            LockSupport.park();
        }

        @Override
        public void unpark(Thread thread) {
            LockSupport.unpark(thread);
        }

    }

    static class ReentrantLockInterruptibly implements RunnableHold {

        private static final ReentrantLock LOCK = new ReentrantLock();

        private final Runnable runnable = () -> {
            // 2.在Runnable定义同步代码块
            String name = Thread.currentThread().getName();
            try {
                LOCK.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(name + " end on InterruptedException");
                return;
            }
            try {
                System.out.println(name + " enter synchronized code");
                // 保证不退出同步代码块
                park(name);
                System.out.println(name + " exit synchronized code");
            } finally {
                LOCK.unlock();
            }
        };

        @Override
        public Runnable getRunnable() {
            return runnable;
        }

        @Override
        public void park(String name) {
            try {
                TimeUnit.MINUTES.sleep(60);
            } catch (InterruptedException e) {
                System.out.println(name + " unpark on " + e);
            }
        }

        @Override
        public void unpark(Thread thread) {
            if (thread.isInterrupted()) {
                return;
            }
            thread.interrupt();
        }
    }
}

