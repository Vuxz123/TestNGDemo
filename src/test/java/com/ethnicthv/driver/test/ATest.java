package com.ethnicthv.driver.test;

import com.ethnicthv.driver.Recorder;
import com.ethnicthv.driver.TestDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;

public class ATest {
    int[] arr = new int[10000];
    //Init the arr
    @BeforeTest
    public void setUp() {
        //random numbers
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 1000);
        }
    }

    @Test
    public void test1() {
        TestDriver driver = new TestDriver();
        //make a copy of arr;
        int[] arrCopy = Arrays.copyOf(arr, arr.length);
        int[] arrCopy2 = Arrays.copyOf(arr, arr.length);
        Recorder recorder = driver.record();
        //use bubble sort
        for (int i = 0; i < arrCopy.length; i++) {
            for (int j = i; j < arrCopy.length; j++) {
                if (arrCopy[i] > arrCopy[j]) {
                    int temp = arrCopy[i];
                    arrCopy[i] = arrCopy[j];
                    arrCopy[j] = temp;
                }
            }
        }
        recorder.stop();
        Arrays.sort(arrCopy2);
        Assert.assertEquals(arrCopy, arrCopy2);
        double v = recorder.getMaxData().cpuUsage();
        System.out.println("Max CPU Usage: " + v);
        Assert.assertTrue(v < 40);
    }

    @Test
    public void test2(){
        TestDriver driver = new TestDriver();
        //make a copy of arr;
        int[] arrCopy = Arrays.copyOf(arr, arr.length);
        int[] arrCopy2 = Arrays.copyOf(arr, arr.length);
        Recorder recorder = driver.record();
        //use insertion sort
        for (int i = 1; i < arrCopy.length; i++) {
            int j = i - 1;
            int key = arrCopy[i];
            while (j >= 0 && arrCopy[j] > key) {
                arrCopy[j + 1] = arrCopy[j];
                j--;
            }
            arrCopy[j + 1] = key;
        }
        recorder.stop();
        Arrays.sort(arrCopy2);
        Assert.assertEquals(arrCopy, arrCopy2);
        double v = recorder.getMaxData().cpuUsage();
        System.out.println("Max CPU Usage: " + v);
        Assert.assertTrue(v < 40);
    }

}
