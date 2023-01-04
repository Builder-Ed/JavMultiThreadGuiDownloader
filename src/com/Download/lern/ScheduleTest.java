package com.Download.lern;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {
    public static void main(String[] args) {
        ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        //Start in 2 Seconds,run every 3 seconds
        s.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis());
        try{
           TimeUnit.SECONDS.sleep(4);
        }catch (InterruptedException e ) {
            e.printStackTrace();
        }
            },2,3,TimeUnit.SECONDS);
    }

    public static void schedule(){
        ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        s.schedule(() -> System.out.println(Thread.currentThread().getName()), 2, TimeUnit.SECONDS);
        s.shutdown();
    }
}
