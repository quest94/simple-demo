package com.quest94.demo.test;

import java.time.Duration;
import java.time.Instant;

public class SafepointTest {

    static int algorithm(int n) {
        int bestSoFar = 0;
        for (int i = 0; i < n; ++i) {
            if (Thread.interrupted()) {
                System.out.println(Thread.currentThread().getName() + " broken by interrupted ");
                break;
            }
            // 增加pow计算，增加计算量，防止循环执行不超过1s就结束了
            bestSoFar = (int) Math.pow(i, 0.3);
        }
        return bestSoFar;
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            Instant start = Instant.now();
            int bestSoFar = algorithm(1000000000);
            double durationInMillis = Duration.between(start, Instant.now()).toMillis();
            System.out.println(Thread.currentThread().getName() + " after " + durationInMillis + " ms, the result is "
                    + bestSoFar);
        };

        // 延迟1ms之后interrupt
        Thread t = new Thread(task, "1");
        t.start();
        Thread.sleep(1);
        t.interrupt();

        // 延迟10ms之后interrupt
        t = new Thread(task, "10");
        t.start();
        Thread.sleep(10);
        t.interrupt();

        // 延迟100ms之后interrupt
        t = new Thread(task, "100");
        t.start();
        Thread.sleep(100);
        t.interrupt();

        // 延迟1s之后interrupt
        // 这时候 algorithm 里面的for循环调用次数应该足够了，会发生代码即时编译优化并 OSR
        t = new Thread(task, "1000");
        t.start();
        Thread.sleep(1000);
        // 发现线程这次不会对 interrupt 有反应了
        t.interrupt();
    }

}
