package commons;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    private static final String REPORT_LOCATION = System.getProperty("user.dir") + "/Reports/";
    public static ExtentReports Reporter = null;
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    static Map<String, ExtentReports> reporterMap = new HashMap<>();

    public static ExtentTest getTest() {
        return extentTestMap.get((int) (Thread.currentThread().threadId()));
    }

    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = Reporter.createTest(testName, description);
        System.out.println("Extent Report Created for Test:"+testName);
        extentTestMap.put((int) (Thread.currentThread().threadId()), test);

        return test;
    }

    public static synchronized ExtentTest createTest(String testName) {
        return createTest(testName, "");
    }

    public static synchronized void flush(String testName) {
        reporterMap.get(testName).flush();
    }

    public static synchronized ExtentReports createReporter(String suiteName, String testClassName) {
        try {
            createReportLocation(suiteName);
            reporterMap.computeIfAbsent(testClassName, key -> Reporter = new ExtentReports());
            ExtentReports Reporter = reporterMap.get(testClassName);
            ExtentSparkReporter fullReports = new ExtentSparkReporter(REPORT_LOCATION + suiteName + testClassName + ".html");
            ExtentSparkReporter failReports = new ExtentSparkReporter(REPORT_LOCATION + suiteName + testClassName + "_fail.html")
                    .filter()
                    .statusFilter().as(new Status[]{Status.FAIL}).apply();
            Reporter.attachReporter(fullReports, failReports);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reporterMap.get(testClassName);
    }

    private static void createReportLocation(String suiteName) {
        File file = new File(REPORT_LOCATION.concat(suiteName));
        file.mkdir();
    }

    public static void fail(String logDetails) {
        getTest().log(Status.FAIL, logDetails);
    }

    public static void pass(String logDetails) {
        getTest().log(Status.PASS, logDetails);
    }

    public static void info(String logDetails) {
        getTest().log(Status.INFO, logDetails);
    }


    public static void logScreenShot(ExtentTest extentTest,String base64String) {
        String imgTag = "<img src =\"" + "base64String" + "\"/>";
        extentTest.info(imgTag.replace("base64String", base64String));
    }

}