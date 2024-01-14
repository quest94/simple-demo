package com.quest94.demo.print.alternate;

import java.util.concurrent.locks.LockSupport;

/**
 * @author : msf
 * @date : 2022/12/2
 * 交替输出
 */
public class AlternatePrint {

    static Thread threadA = null;
    static Thread threadB = null;

    public static void main(String[] args) {
        String numberA = "123456";
        String characterB = "abcdef";

        threadA = new Thread(() -> {
            for (char c : numberA.toCharArray()) {
                System.out.print(c);
                LockSupport.unpark(threadB);
                LockSupport.park();
            }
        });
        threadB = new Thread(() -> {
            for (char c : characterB.toCharArray()) {
                LockSupport.park();
                System.out.print(c);
                LockSupport.unpark(threadA);
            }
        });
        threadA.start();
        threadB.start();
    }
}