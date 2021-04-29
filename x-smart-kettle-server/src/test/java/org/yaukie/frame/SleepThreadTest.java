package org.yaukie.frame;

import org.apache.poi.ss.formula.functions.T;

import java.util.Date;

public class SleepThreadTest {
    public static void main(String[] args){
        CountOperate c= new CountOperate();
        Thread t1=new Thread(c);
        System.out.println("main begin t1 isAlive="+t1.isAlive());
        t1.setName("A");
        t1.start();
        while (t1.isAlive()){
            try {
                Thread.sleep(1L);
                System.out.println("等待1秒后");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("main end t1 isAlive="+t1.isAlive());

    }

    public static class CountOperate extends Thread{
        public CountOperate() {
            System.out.println("构造方法里——测试开始");
            System.out.println("Thread.currentThread().getName="+Thread.currentThread().getName());
            System.out.println("Thread.currentThread().isAlive()="+Thread.currentThread().isAlive());
            System.out.println("This.getName="+this.getName());
            System.out.println("This.isAlive="+this.isAlive());
            System.out.println("构造方法测试结束");
        }
        @Override
        public void run() {
            System.out.println("run方法里——测试开始");
            System.out.println("Thread.currentThread().getName="+Thread.currentThread().getName());
            System.out.println("Thread.currentThread().isAlive()="+Thread.currentThread().isAlive());
            System.out.println("This.getName="+this.getName());
            System.out.println("This.isAlive="+this.isAlive());
            System.out.println("run方法结束");
        }

    }

}

