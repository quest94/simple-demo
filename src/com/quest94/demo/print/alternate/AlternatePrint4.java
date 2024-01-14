package com.quest94.demo.print.alternate;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * @author : msf
 * @date : 2022/12/2
 * 交替输出
 */
public class AlternatePrint4 {

    public static void main(String[] args) {
        String numberA = "123456";
        String characterB = "abcdef";
        TransferQueue<Character> queue = new LinkedTransferQueue<>();

        Thread threadA = new Thread(() -> {
            try {
                for (char c : numberA.toCharArray()) {
                    queue.transfer(c);
                    System.out.print(queue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                for (char c : characterB.toCharArray()) {
                    System.out.print(queue.take());
                    queue.transfer(c);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadB.start();
        threadA.start();
    }

}