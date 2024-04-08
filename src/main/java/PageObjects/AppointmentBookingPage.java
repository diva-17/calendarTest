package PageObjects;

import com.aventstack.extentreports.ExtentTest;
import commons.ExtentTestManager;
import commons.WebDriverHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;


public class AppointmentBookingPage {

    WebDriver driver;

    public AppointmentBookingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "book-appointment")
    private WebElement newAppointmentButton;

    @FindBy(xpath = "//div[@class='n-input-wrapper']/div[1]/input")
    private WebElement appointmentTitle;

    @FindBy(id = "add-description-text")
    private WebElement addDescriptionButton;

    @FindBy(xpath = "//textarea[@class='n-input__textarea-el']")
    private WebElement addDescription;

    @FindBy(id = "select-timezone-dropdown")
    private WebElement timezoneDropdown;

    @FindBy(xpath = "//div[@class='searchInputContainer']")
    private WebElement selectContactButton;

    @FindBy(xpath = "(//*[name()='svg'][contains(@class,'w-5 h-5 text-gray-600')])[2]")
    private WebElement selectContact;

    @FindBy(id = "contact-0")
    private WebElement clickContact;

    @FindBy(id = "modal-footer-btn-positive-action")
    private WebElement bookAppointment;

    @FindBy(id = "sb_calendars")
    private WebElement calendarOption;


    @FindBy(xpath = "//div[contains(@class, 'n-base-select-option__content') and contains(string(),'GMT-10:00 Pacific/Honolulu (HST)')]")
    private WebElement timezoneSelect;
    String timeZoneXpath = "//div[contains(@class, 'n-base-select-option__content') and contains(string(),'<timeZone>')]";

    @FindBy(xpath = "//div[@class='bookAppointmentFormContainer']")
    private WebElement appointmentContainer;

    @FindBy(xpath = "(//div[@class='n-base-selection-label'])[4]")
    private WebElement slotDropdown;

    @FindBy(xpath = "(//div[contains(@class, 'n-select hl-select')])[4]")
    List<WebElement> slotSelection;

    @FindBy(id = "slot-picker-standard")
    private WebElement selectedSlotTime;

    @FindBy(id = "date-picker-standard")
    private WebElement dateSelected;

    @FindBy(id = "tb_apppontment-tab")
    private WebElement appointmentTab;

    @FindBy(xpath="(//button[@class='n-button n-button--default-type n-button--medium-type quaternary icon-only'])[1]")
    private WebElement appointmentDetails;

    @FindBy(xpath = "(//div[@class='n-dropdown-option-body__label'])[1]")
    private WebElement viewAppointmentdetails;

    @FindBy(xpath = "(//div[@class='flex flex-col items-center justify-between'])[1]")
    private WebElement appointmentDateTime;

    @FindBy(id = "date-picker-standard")
    private WebElement datePicker;


    // Opening Appointment tab in calendars module
    public void openAppointmentSection(){

        //Created node for the Calendar page to log the screenshot and data info
        ExtentTest datalog = ExtentTestManager.getTest().createNode("Calendars Page");
        WebDriverHandler.waitTillVisible(calendarOption);
        calendarOption.click();

        ExtentTestManager.logScreenShot(datalog, WebDriverHandler.takeScreenShot(driver));
        datalog.info("Calendar Page open success");

        WebDriverHandler.waitTillVisible(appointmentTab);
        appointmentTab.click();
    }


   // Selecting new appointment
    public void clickNewAppointment(){

        // Switching to iframe for appointment details page
        WebElement iframeElement = driver.findElements(By.tagName("iframe")).get(0);
        driver.switchTo().frame(iframeElement);

        WebDriverHandler.waitTillVisible(newAppointmentButton);
        newAppointmentButton.click();

        // Switching to default content as book new appointment page is not in iframe
        driver.switchTo().defaultContent();

    }


    // New appointment booking
    public String bookNewAppointment(Map<String, String> testdata) throws InterruptedException {
        openAppointmentSection();
        clickNewAppointment();

        // send appointment description from testdata
        WebDriverHandler.waitTillVisible(appointmentTitle);
        appointmentTitle.sendKeys(testdata.get("Appointment_Description"));

        WebDriverHandler.waitTillVisible(addDescriptionButton);
        addDescriptionButton.click();

        // Sending the appointment description from testdata
        addDescription.sendKeys(testdata.get("Appointment_Description"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", timezoneDropdown);
        timezoneDropdown.click();

        String timezoneValue = testdata.get("customer_time_zone");
        WebElement timeZoneDropDown = driver.findElements(By.xpath(timeZoneXpath.replace("<timeZone>",timezoneValue))).get(0);
        js.executeScript("arguments[0].scrollIntoView();", timeZoneDropDown);
        timeZoneDropDown.click();

        String appointmentDate = testdata.get("appointmentDate");
        String selectedDate = selectAppointmentDate(appointmentDate);


        String selectedTimeSlot = selectRandomTimeslot();
        selectContactForBooking();

        WebDriverHandler.waitTillVisible(bookAppointment);
        bookAppointment.click();
        System.out.println("booked appointment details" + selectedDate.concat(selectedTimeSlot));

       return selectedDate.concat(selectedTimeSlot);

    }


    // Selecting the contact mandatory field for booking the appointment
    public void selectContactForBooking(){
        selectContactButton.click();
        selectContact.click();
        clickContact.click();
    }


    // Choosing the random timeslot
    public String selectRandomTimeslot() throws InterruptedException {
        slotDropdown.click();
        List<WebElement> timeSlot = slotSelection;
        Random rand = new Random();
        int randomTimeSelect = rand.nextInt(timeSlot.size());
        timeSlot.get(randomTimeSelect).click();

        String selectedTimeSlot = selectedSlotTime.getText();
        String[] timeSplit = selectedTimeSlot.split(" - ");
        String time = timeSplit[0];
        System.out.println("time slot selected" +time);
        return time;
    }


    // Getting the booked appointment details in appointment tab
    public String getBookedAppointmentDetails() throws InterruptedException {
        ExtentTest datalog = ExtentTestManager.getTest().createNode("Appointment list");


        WebElement iframeElement = driver.findElements(By.tagName("iframe")).get(0);
        driver.switchTo().frame(iframeElement);

        WebDriverHandler.waitTillVisible(appointmentDateTime);
        ExtentTestManager.logScreenShot(datalog, WebDriverHandler.takeScreenShot(driver));

        datalog.info("Appointment Booked successfully");

        return appointmentDateTime.getAttribute("textContent");
    }


    // Choosing the date for appointment booking
    public String selectAppointmentDate(String date){
        try {
            datePicker.click();
            Thread.sleep(2000);
            WebElement dateToSelect = driver.findElements(By.xpath("//div[contains(@class, 'vdpCellContent') and text() = '" + date.replaceAll("(?<=\\d)(st|nd|rd|th)", "") + "']")).get(0);
            dateToSelect.click();

            return  "Apr " + date + ", 2024, ";

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
