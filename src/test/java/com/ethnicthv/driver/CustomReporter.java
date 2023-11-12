package com.ethnicthv.driver;

import org.testng.*;
import org.testng.reporters.IReporterConfig;
import org.testng.xml.XmlSuite;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CustomReporter implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        // Create a StringBuilder to build the HTML content
        StringBuilder htmlContent = new StringBuilder();

        // Add HTML head and CSS styles
        htmlContent.append("<html><head>\n")
                .append("<style>\n")
                .append(".header { background-color: #1E1E1E; color: #FFFFFF; padding: 10px; }\n")
                .append(".suite { margin: 10px 0; }\n")
                .append(".clazz { margin: 10px 0; padding: 10px; border: 1px solid #DDDDDD;}\n")
                .append(".test { padding: 10px; border: 1px solid #DDDDDD; }\n")
                .append(".test .name { font-weight: bold; }\n")
                .append(".test .status { margin-top: 5px; margin-left: 20px}\n")
                .append(".test .status.passed { color: #008000; }\n")
                .append(".test .status.failed { color: #FF0000; }\n")
                .append(".test .status.skipped { color: #FFA500; }\n")
                .append("</style>\n")
                .append("</head><body>\n");

        // Iterate over the test suites
        for (ISuite suite : suites) {
            htmlContent.append("<div class=\"suite\">\n");

            // Add suite name
            htmlContent.append("<div class=\"header\">\n")
                    .append("<h2>").append(suite.getName()).append("</h2>\n")
                    .append("</div>\n");
            // Iterate over the test results of the suite
//            Map<String, ISuiteResult> results = suite.getResults();
//            for (ISuiteResult result : results.values()) {
//                // Add test name and status
//                htmlContent.append("<div class=\"test\">")
//                        .append("<div class=\"name\">").append(result.getTestContext().getName()).append("</div>")
//                        .append("<div class=\"status ").append("\">").append(result).append("</div>")
//                        .append("</div>");
//            }
            final AtomicReference<IClass> iClass = new AtomicReference<>(null);
            System.out.println("a");
            var list = getiInvokedMethods(suite);
            System.out.println("a1");
            list.forEach(iInvokedMethod -> {
                if(iInvokedMethod.isConfigurationMethod()) return;
                IClass iClass1 = iInvokedMethod.getTestMethod().getTestClass();
                //if(iClass.get() != null && !iClass.get().equals(iClass1)) htmlContent.append("</div>\n");
                if(iClass.get() == null || !iClass.get().equals(iClass1)){
                    if(iClass.get() != null) htmlContent.append("</div>\n");
                    htmlContent.append("<div class=\"clazz\">\n")
                            .append("<div class=\"name\">").append(iClass1.getName()).append("</div>\n");
                    iClass.set(iClass1);
                }
                ITestResult testResult = iInvokedMethod.getTestResult();
                ITestContext testContext = testResult.getTestContext();
                htmlContent.append("<div class=\"test\">\n")
                        .append("<div class=\"name\">").append(testResult.getName()).append("</div>\n")
                        .append("<div>Class: ").append(testResult.getTestClass().getName()).append("</div>\n")
                        .append("<div class=\"status ").append(getStatus(testResult)).append("\">\n")
                        .append("<div>Status: ").append(getStatus(testResult)).append("</div>\n")
                        .append("<div>Start Time: ").append(testContext.getStartDate()).append("</div>\n")
                        .append("<div>End Time: ").append(testContext.getEndDate()).append("</div>\n")
                        .append("<div>Duration: ").append(testContext.getEndDate().getTime() - testContext.getStartDate().getTime()).append("ms</div>\n")
                        .append("</div>\n").append("</div>\n");
            });
            htmlContent.append("</div>\n");
        }
        htmlContent.append("</div>\n");
        // Add closing HTML tags
        htmlContent.append("</body></html>");
        System.out.println("a2");
        // Write the HTML content to a file
        try (FileWriter fileWriter = new FileWriter(outputDirectory + "/TestOutput.html")) {
            fileWriter.write(htmlContent.toString());
            System.out.println("a3");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<IInvokedMethod> getiInvokedMethods(ISuite suite) {
        var list = suite.getAllInvokedMethods();
        //sort the list to make sure the order is correct using bubble sort
        for (int i = 0; i < list.size(); i++) {
            for (int j = i; j < list.size(); j++) {
                var iInvokedMethod1 = list.get(i);
                var iInvokedMethod2 = list.get(j);
                if(iInvokedMethod1.getTestResult().getTestClass().getName().compareTo(iInvokedMethod2.getTestResult().getTestClass().getName()) != 0){
                    list.set(i, iInvokedMethod2);
                    list.set(j, iInvokedMethod1);
                }
            }
        }
        return list;
    }

    //get status of a test
    public static String getStatus(ITestResult result){
        return switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "passed";
            case ITestResult.FAILURE -> "failed";
            case ITestResult.SKIP -> "skipped";
            default -> "unknown";
        };
    }

    @Override
    public IReporterConfig getConfig() {
        return IReporter.super.getConfig();
    }
}
