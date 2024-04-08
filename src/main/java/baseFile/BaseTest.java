package baseFile;

import commons.ExtentTestManager;
import commons.PropertyLoader;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.text.SimpleDateFormat;
import java.util.Date;


public class BaseTest {
    static String suiteName, testClassName;
    final static String dateTimeString = new SimpleDateFormat("MMddhhmm").format(new Date());
    public PropertyLoader propertyLoader = new PropertyLoader();


    @BeforeClass
    public synchronized void createTest(final ITestContext testContext) {
        testClassName = this.getClass().getSimpleName();
        suiteName = (suiteName == null) ? testContext.getCurrentXmlTest().getSuite().getName().concat("_" + dateTimeString + "/") : suiteName;
        ExtentTestManager.createReporter(suiteName, testClassName);
    }

    @AfterMethod
    public void clearResource() {
        ExtentTestManager.flush(testClassName);
    }


}