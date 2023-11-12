package com.ethnicthv;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class Main {
    public static void main(String[] args) {
        //create a new Thread to use for the example
        Thread thread = getThread();
        //start the thread
        thread.start();
        //create an array of strings to use for the example
        String[] array = new String[1000000];
        //loop through the array and set each element to a new string
        for (int i = 0; i < 1000000; i++) {
            array[i] = new String("abc");
        }
        //wait for some time to allow the thread to run
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //get the operating system bean
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        // Get the current CPU usage
        double cpuUsage = osBean.getProcessCpuLoad() * 100;

        // Print the CPU usage
        System.out.println("CPU Usage: " + cpuUsage + "%" + "\nAvailable processors (cores): " + Runtime.getRuntime().availableProcessors());
        //get the bean
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        //print the memory usage
        System.out.println("Heap memory usage: " + memoryBean.getHeapMemoryUsage());
        System.out.println("Non-Heap memory usage: " + memoryBean.getNonHeapMemoryUsage());
        //format the memory usage into a string that looks like the jstat command output
        String s = String.format("%-20s%-12s%-12s%-12s%-12s", "Heap memory usage:", "init", "used", "committed", "max");
        System.out.println(s);
        s = String.format("%-20s%-12d%-12d%-12d%-12d", "", memoryBean.getHeapMemoryUsage().getInit(),
                memoryBean.getHeapMemoryUsage().getUsed(),
                memoryBean.getHeapMemoryUsage().getCommitted(),
                memoryBean.getHeapMemoryUsage().getMax());
        System.out.println(s);
        s = String.format("%-20s%-12s%-12s%-12s%-12s", "Non-Heap memory usage:", "init", "used", "committed", "max");
        System.out.println(s);
        s = String.format("%-20s%-12d%-12d%-12d%-12d", "", memoryBean.getNonHeapMemoryUsage().getInit(),
                memoryBean.getNonHeapMemoryUsage().getUsed(),
                memoryBean.getNonHeapMemoryUsage().getCommitted(),
                memoryBean.getNonHeapMemoryUsage().getMax());
        System.out.println(s);
        //print the number of objects in memory
        System.out.println("Number of objects in memory: " + ManagementFactory.getMemoryPoolMXBeans().stream()
                .mapToLong(memoryPoolMXBean -> memoryPoolMXBean.getUsage().getUsed()).sum());
        //print the number of threads
        System.out.println("Number of threads: " + ManagementFactory.getThreadMXBean().getThreadCount());
        //print the number of classes loaded
        System.out.println("Number of classes loaded: " + ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
        //print the cpu usage
        System.out.println("Process CPU time: " + ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
        //do somthing to change the System load average for the last minute
        for (int i = 0; i < 1000000; i++) {
            array[i].contains("abc");
        }
        //print system load average for the last minute
        System.out.println("System load average for the last minute: " + ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());

        System.out.println("Hello world!");
    }

    private static Thread getThread() {
        Thread thread = new Thread(() -> {
            //loop forever
            while (true) {
                //print the current time
                System.out.println("Current time: " + System.currentTimeMillis());
                //sleep for 1 second
                try {
                    Thread.sleep(1000);
                    System.out.println("a");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //set the thread to be a daemon thread so that it does not block the JVM from exiting
        thread.setDaemon(true);
        return thread;
    }
}