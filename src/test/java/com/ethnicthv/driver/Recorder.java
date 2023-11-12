package com.ethnicthv.driver;

import com.ethnicthv.driver.observer.Data;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Stack;

public class Recorder {
    private boolean isRecording = true;
    private final Stack<Data> data = new Stack<>();
    private final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    public void record() {
        //Fire a thread that runs every 1 second and record the data
        System.out.println("Start recording");
        isRecording = true;
        Thread thread = new Thread(
                () -> {
                    while (isRecording) {
                        getRecord();
                    }
                }
        );
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        //Stop the thread
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Stop recording");
        isRecording = false;
    }

    public void getRecord() {
        //record the data
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        Data d= new Data(
                osBean.getProcessCpuLoad() * 100,
                osBean.getAvailableProcessors(),
                heapMemoryUsage.getInit(),
                heapMemoryUsage.getUsed(),
                heapMemoryUsage.getCommitted(),
                heapMemoryUsage.getMax(),
                nonHeapMemoryUsage.getInit(),
                nonHeapMemoryUsage.getUsed(),
                nonHeapMemoryUsage.getCommitted(),
                nonHeapMemoryUsage.getMax(),
                ManagementFactory.getMemoryPoolMXBeans().stream()
                        .mapToLong(memoryPoolMXBean -> memoryPoolMXBean.getUsage().getUsed()).sum(),
                ManagementFactory.getThreadMXBean().getThreadCount(),
                ManagementFactory.getClassLoadingMXBean().getLoadedClassCount()
        );
        data.push(d);
    }

    public Data[] getRecords() {
        //return an immutable copy of the data
        return data.toArray(new Data[0]);
    }

    public void clearRecords() {
        data.clear();
    }

    public Data getMaxData() {
        //Get max of each field
        double cpuUsage = 0;
        int availableProcessors = 0;
        long heapMemoryUsageInit = 0;
        long heapMemoryUsageUsed = 0;
        long heapMemoryUsageCommitted = 0;
        long heapMemoryUsageMax = 0;
        long nonHeapMemoryUsageInit = 0;
        long nonHeapMemoryUsageUsed = 0;
        long nonHeapMemoryUsageCommitted = 0;
        long nonHeapMemoryUsageMax = 0;
        long numberOfObjectsInMemory = 0;
        int numberOfThreads = 0;
        int numberOfClassesLoaded = 0;
        for (Data d : data.toArray(new Data[0])) {
            cpuUsage = Math.max(cpuUsage, d.cpuUsage());
            availableProcessors = Math.max(availableProcessors, d.availableProcessors());
            heapMemoryUsageInit = Math.max(heapMemoryUsageInit, d.heapMemoryUsageInit());
            heapMemoryUsageUsed = Math.max(heapMemoryUsageUsed, d.heapMemoryUsageUsed());
            heapMemoryUsageCommitted = Math.max(heapMemoryUsageCommitted, d.heapMemoryUsageCommitted());
            heapMemoryUsageMax = Math.max(heapMemoryUsageMax, d.heapMemoryUsageMax());
            nonHeapMemoryUsageInit = Math.max(nonHeapMemoryUsageInit, d.nonHeapMemoryUsageInit());
            nonHeapMemoryUsageUsed = Math.max(nonHeapMemoryUsageUsed, d.nonHeapMemoryUsageUsed());
            nonHeapMemoryUsageCommitted = Math.max(nonHeapMemoryUsageCommitted, d.nonHeapMemoryUsageCommitted());
            nonHeapMemoryUsageMax = Math.max(nonHeapMemoryUsageMax, d.nonHeapMemoryUsageMax());
            numberOfObjectsInMemory = Math.max(numberOfObjectsInMemory, d.numberOfObjectsInMemory());
            numberOfThreads = Math.max(numberOfThreads, d.numberOfThreads());
            numberOfClassesLoaded = Math.max(numberOfClassesLoaded, d.numberOfClassesLoaded());
        }
        return new Data(cpuUsage, availableProcessors, heapMemoryUsageInit, heapMemoryUsageUsed, heapMemoryUsageCommitted, heapMemoryUsageMax, nonHeapMemoryUsageInit, nonHeapMemoryUsageUsed, nonHeapMemoryUsageCommitted, nonHeapMemoryUsageMax, numberOfObjectsInMemory, numberOfThreads, numberOfClassesLoaded);
    }

    public Data getMinData() {
        double cpuUsage = Double.MAX_VALUE;
        int availableProcessors = Integer.MAX_VALUE;
        long heapMemoryUsageInit = Long.MAX_VALUE;
        long heapMemoryUsageUsed = Long.MAX_VALUE;
        long heapMemoryUsageCommitted = Long.MAX_VALUE;
        long heapMemoryUsageMax = Long.MAX_VALUE;
        long nonHeapMemoryUsageInit = Long.MAX_VALUE;
        long nonHeapMemoryUsageUsed = Long.MAX_VALUE;
        long nonHeapMemoryUsageCommitted = Long.MAX_VALUE;
        long nonHeapMemoryUsageMax = Long.MAX_VALUE;
        long numberOfObjectsInMemory = Long.MAX_VALUE;
        int numberOfThreads = Integer.MAX_VALUE;
        int numberOfClassesLoaded = Integer.MAX_VALUE;
        for (Data d : data.toArray(new Data[0])) {
            cpuUsage = Math.min(cpuUsage, d.cpuUsage());
            availableProcessors = Math.min(availableProcessors, d.availableProcessors());
            heapMemoryUsageInit = Math.min(heapMemoryUsageInit, d.heapMemoryUsageInit());
            heapMemoryUsageUsed = Math.min(heapMemoryUsageUsed, d.heapMemoryUsageUsed());
            heapMemoryUsageCommitted = Math.min(heapMemoryUsageCommitted, d.heapMemoryUsageCommitted());
            heapMemoryUsageMax = Math.min(heapMemoryUsageMax, d.heapMemoryUsageMax());
            nonHeapMemoryUsageInit = Math.min(nonHeapMemoryUsageInit, d.nonHeapMemoryUsageInit());
            nonHeapMemoryUsageUsed = Math.min(nonHeapMemoryUsageUsed, d.nonHeapMemoryUsageUsed());
            nonHeapMemoryUsageCommitted = Math.min(nonHeapMemoryUsageCommitted, d.nonHeapMemoryUsageCommitted());
            nonHeapMemoryUsageMax = Math.min(nonHeapMemoryUsageMax, d.nonHeapMemoryUsageMax());
            numberOfObjectsInMemory = Math.min(numberOfObjectsInMemory, d.numberOfObjectsInMemory());
            numberOfThreads = Math.min(numberOfThreads, d.numberOfThreads());
            numberOfClassesLoaded = Math.min(numberOfClassesLoaded, d.numberOfClassesLoaded());
        }
        return new Data(cpuUsage, availableProcessors, heapMemoryUsageInit, heapMemoryUsageUsed, heapMemoryUsageCommitted, heapMemoryUsageMax, nonHeapMemoryUsageInit, nonHeapMemoryUsageUsed, nonHeapMemoryUsageCommitted, nonHeapMemoryUsageMax, numberOfObjectsInMemory, numberOfThreads, numberOfClassesLoaded);
    }

    public Data getAverageData() {
        //get the average of each field
        double cpuUsage = 0;
        int availableProcessors = 0;
        long heapMemoryUsageInit = 0;
        long heapMemoryUsageUsed = 0;
        long heapMemoryUsageCommitted = 0;
        long heapMemoryUsageMax = 0;
        long nonHeapMemoryUsageInit = 0;
        long nonHeapMemoryUsageUsed = 0;
        long nonHeapMemoryUsageCommitted = 0;
        long nonHeapMemoryUsageMax = 0;
        long numberOfObjectsInMemory = 0;
        int numberOfThreads = 0;
        int numberOfClassesLoaded = 0;
        for (Data d : data.toArray(new Data[0])) {
            cpuUsage += d.cpuUsage();
            availableProcessors += d.availableProcessors();
            heapMemoryUsageInit += d.heapMemoryUsageInit();
            heapMemoryUsageUsed += d.heapMemoryUsageUsed();
            heapMemoryUsageCommitted += d.heapMemoryUsageCommitted();
            heapMemoryUsageMax += d.heapMemoryUsageMax();
            nonHeapMemoryUsageInit += d.nonHeapMemoryUsageInit();
            nonHeapMemoryUsageUsed += d.nonHeapMemoryUsageUsed();
            nonHeapMemoryUsageCommitted += d.nonHeapMemoryUsageCommitted();
            nonHeapMemoryUsageMax += d.nonHeapMemoryUsageMax();
            numberOfObjectsInMemory += d.numberOfObjectsInMemory();
            numberOfThreads += d.numberOfThreads();
            numberOfClassesLoaded += d.numberOfClassesLoaded();
        }
        cpuUsage /= data.size();
        availableProcessors /= data.size();
        heapMemoryUsageInit /= data.size();
        heapMemoryUsageUsed /= data.size();
        heapMemoryUsageCommitted /= data.size();
        heapMemoryUsageMax /= data.size();
        nonHeapMemoryUsageInit /= data.size();
        nonHeapMemoryUsageUsed /= data.size();
        nonHeapMemoryUsageCommitted /= data.size();
        nonHeapMemoryUsageMax /= data.size();
        numberOfObjectsInMemory /= data.size();
        numberOfThreads /= data.size();
        numberOfClassesLoaded /= data.size();
        return new Data(cpuUsage, availableProcessors, heapMemoryUsageInit, heapMemoryUsageUsed, heapMemoryUsageCommitted, heapMemoryUsageMax, nonHeapMemoryUsageInit, nonHeapMemoryUsageUsed, nonHeapMemoryUsageCommitted, nonHeapMemoryUsageMax, numberOfObjectsInMemory, numberOfThreads, numberOfClassesLoaded);
    }

    public String print() {
        //print the number of records, max, min, and average
        return "Number of records: " + data.size() + "\n" +
                "Max: " + getMaxData() + "\n" +
                "Min: " + getMinData() + "\n" +
                "Average: " + getAverageData() + "\n";
    }
}
