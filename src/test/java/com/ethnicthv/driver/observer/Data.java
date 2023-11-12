package com.ethnicthv.driver.observer;

public record Data(double cpuUsage,
        int availableProcessors,
        long heapMemoryUsageInit,
        long heapMemoryUsageUsed,
        long heapMemoryUsageCommitted,
        long heapMemoryUsageMax,
        long nonHeapMemoryUsageInit,
        long nonHeapMemoryUsageUsed,
        long nonHeapMemoryUsageCommitted,
        long nonHeapMemoryUsageMax,
        long numberOfObjectsInMemory,
        int numberOfThreads,
        int numberOfClassesLoaded) {

}
