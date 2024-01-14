package com.quest94.demo.print.alternate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : msf
 * @date : 2022/12/2
 * 交替输出
 */
public class AlternatePrint3 {

    public static void main(String[] args) {
        String numberA = "123456";
        String characterB = "abcdef";
        ReentrantLock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        CountDownLatch latch = new CountDownLatch(1);

        Thread threadA = new Thread(() -> {
            lock.lock();
            try {
                for (char c : numberA.toCharArray()) {
                    System.out.print(c);
                    try {
                        // 唤醒线程b
                        latch.countDown();
                        conditionB.signal();
                        // 线程A阻塞
                        conditionA.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                conditionB.signal();
            } finally {
                lock.unlock();

            }

        });

        Thread threadB = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.lock();
            try {
                for (char c : characterB.toCharArray()) {
                    try {

                        System.out.print(c);
                        // 唤醒线程a
                        conditionA.signal();
                        // 线程b阻塞
                        conditionB.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }

        });

        threadB.start();
        threadA.start();
    }

}