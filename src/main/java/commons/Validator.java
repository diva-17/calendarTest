package commons;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.asserts.SoftAssert;

public class Validator {

    public static <T> void verify(T expected, T actual, String message) {

        verify(expected, actual, message, ExtentTestManager.getTest());
    }

    public static <T> void verify(T expected, T actual, String message, ExtentTest report) {
        try {
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(actual, expected);
            softAssert.assertAll();
            report.log(Status.PASS, "<pre><b style=color:green>" + message + "</br>" + actual + "</b></pre>");
        } catch (AssertionError e) {
            String exceptionMessage = "";
            if (e.getMessage() == null) {
                exceptionMessage = "<b style = color:red>" + e.getMessage() + "</b></br>";
            }
            report.log(Status.FAIL, "<pre>" + exceptionMessage
                    + "<b style=color:red>" + message
                    + "</br> Expected - " + expected +
                    "</br> Actual - " + actual + "</b></pre>");
        }
    }

}