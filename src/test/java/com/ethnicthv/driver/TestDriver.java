package com.ethnicthv.driver;

import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;

public class TestDriver {
    private static TestDriver INSTANCE;
    public static String PACKAGE = "com.ethnicthv.driver.test";

    public static void main(String[] args) throws InterruptedException {
        INSTANCE = new TestDriver();
        TestNG testNG = new TestNG();
        Class[] classes = getTestClasses().toArray(new Class[0]);
        System.out.println(classes.length);
        System.out.println(Arrays.stream(classes).map(Class::getName).reduce((s, s2) -> s + "\n" + s2).orElse(""));
        testNG.setTestClasses(classes);
        testNG.addListener(new CustomReporter());
        //make testNG print the result
        testNG.run();
        Thread.sleep(500);
    }

    //get all BTest Class in a Directory
    public static List<Class> getTestClasses() {
        //Get class those name end with BTest
        List<Class> classes = new LinkedList<>();
        ClassLoader classLoader = TestDriver.class.getClassLoader();
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(PACKAGE.replace(".", "/"));
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                if (directory.exists() && directory.isDirectory()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            String fileName = file.getName();
                            if (fileName.endsWith("Test.class")) {
                                String className = PACKAGE + '.' + fileName.substring(0, fileName.length() - 6);
                                Class<?> clazz = Class.forName(className);
                                classes.add(clazz);
                            }
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    public static TestDriver getINSTANCE() {
        return INSTANCE;
    }

    public Recorder record() {
        Recorder recorder = new Recorder();
        recorder.record();
        return recorder;
    }
}
