package com.quest94.demo.parallel;

import java.util.concurrent.ForkJoinWorkerThread;
import java.util.stream.LongStream;

public class ParallelSumComputationUsingForkJoin1 {

    public static void main(String[] args) {
        System.out.println(new ParallelSumComputationUsingForkJoin1().sumUsingParallel());
    }

    public long sumUsingParallel() {
        return LongStream.rangeClosed(1L, 10L)
                .parallel()
                .peek(this::printThreadName)
                .reduce(0L, this::printSum);
    }

    public void printThreadName(long l) {
        Thread thread = Thread.currentThread();
        String tName = thread.getName();
        if (thread instanceof ForkJoinWorkerThread) {
            int poolSize = ((ForkJoinWorkerThread) thread).getPool().getPoolSize();
            System.out.println(tName + " poolSize:" + poolSize);
        }
        System.out.println(tName + " offers:" + l);

    }

    public long printSum(long i, long j) {
        long sum = i + j;
        String tName = Thread.currentThread().getName();
        System.out.printf(
                "%s has: %d; plus: %d; result: %d\n",
                tName, i, j, sum
        );
        return sum;
    }

}
