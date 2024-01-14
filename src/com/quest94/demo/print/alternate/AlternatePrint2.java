package com.quest94.demo.print.alternate;

import java.util.concurrent.CountDownLatch;

/**
 * @author : msf
 * @date : 2022/12/2
 * 交替输出
 */
public class AlternatePrint2 {

    public static void main(String[] args) {
        String numberA = "123456";
        String characterB = "abcdef";
        Object lock = new Object();
        CountDownLatch latch = new CountDownLatch(1);

        Thread threadA = new Thread(() -> {
            synchronized (lock) {
                for (char c : numberA.toCharArray()) {
                    System.out.print(c);
                    try {
                        // 唤醒线程B
                        latch.countDown();
                        lock.notify();
                        // 线程A阻塞
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 是为了更好的退出，因为程序在结束时候肯定会有一个线程在阻塞住。
                lock.notify();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                for (char c : characterB.toCharArray()) {
                    try {
                        // 唤醒线程A
                        System.out.print(c);
                        lock.notify();
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        threadB.start();
        threadA.start();
    }

}