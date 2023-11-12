package com.ethnicthv.driver.test;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BTest {
    @BeforeTest
    public void setUp() {
    }

    @Test
    public void test() {
        Assert.assertEquals(0, 0);
    }
}
