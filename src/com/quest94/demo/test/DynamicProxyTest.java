package com.quest94.demo.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyTest {

    public static void main(String[] args) {
        Programmer programmer = new Programmer();
        Worker worker = (Worker) Proxy.newProxyInstance(programmer.getClass().getClassLoader(),
                programmer.getClass().getInterfaces(), new WorkHandler(programmer));
        worker.work();
    }

    interface Worker {
        void work();
    }

    static class Programmer implements Worker {

        @Override
        public void work() {
            System.out.println("wording");
        }

    }

    static class WorkHandler implements InvocationHandler {

        private Object target;

        public WorkHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("work")) {
                System.out.println("before");
                Object result = method.invoke(target, args);
                System.out.println("after");
                return result;
            }
            return method.invoke(target, args);
        }
    }
}
