import PageObjects.AppointmentBookingPage;
import PageObjects.LandingPage;
import baseFile.BaseTest;
import commons.ExtentTestManager;
import commons.FileHelper;
import commons.TimeConverter;
import commons.WebDriverHandler;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.*;

import static commons.Validator.verify;

public class AppointmentBookingTest extends BaseTest {

    WebDriver driver;
    Properties properties;

    @BeforeClass
    public void appointmentBookingBeforeClass() {
        properties = propertyLoader.loadQueriesProperty();
    }

    @DataProvider(name = "appointmentData")
    public Object[][] buildTestData() {
        Object[][] array = null;
        try {
            List<HashMap<String, String>> scenarios = FileHelper.getDataFromExcel("testdatasheetNew", properties.getProperty("bookingData"));
            array = new Object[scenarios.size()][1];
            for (int i = 0; i < scenarios.size(); i++) {
                HashMap<String, String> testdata = scenarios.get(i);
                array[i][0] = testdata;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    @BeforeMethod
    public void preSetup(){
        driver = WebDriverHandler.CreateDriver();
    }

    @Test(dataProvider = "appointmentData", testName = "Appointment booking Test")
    public void appointmentBooking(Map<String, String>testData) {
        try {
            ExtentTestManager.createTest("ID : " + testData.get("id") + " - " + testData.get("scenario") + " Customer Time zone " + testData.get("customer_time_zone") );

            LandingPage landingPage = new LandingPage(driver);
            landingPage.performLogin();

            AppointmentBookingPage appointmentBookingPage = new AppointmentBookingPage(driver);
            String bookedAppointmentDateTime = appointmentBookingPage.bookNewAppointment(testData);

            String customerTimeZone = testData.get("customer_time_zone");
            String businessTimeZone = testData.get("business_time_zone");
            ExtentTestManager.info("Expected Appointment details : " + customerTimeZone + " " + businessTimeZone +" "+bookedAppointmentDateTime);
            ExtentTestManager.info("Actual Appointment details : " + bookedAppointmentDateTime);


            String expectedAppointmentDetails = TimeConverter.convertTime(customerTimeZone,businessTimeZone,bookedAppointmentDateTime);
            String bookedAppointmentDetails = appointmentBookingPage.getBookedAppointmentDetails();

            verify(expectedAppointmentDetails,bookedAppointmentDetails,"verify the date and time");


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    public void endTest() {
        WebDriverHandler.quitDriver();
    }

}
