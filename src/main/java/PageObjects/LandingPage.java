package PageObjects;

import baseFile.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import commons.ExtentTestManager;
import commons.WebDriverHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Properties;


public class LandingPage extends BaseTest {

    WebDriver driver;
    Properties properties;
    String url,username,password;

    public LandingPage(WebDriver driver) {
        this.driver = driver;
        properties = propertyLoader.loadQueriesProperty();
        PageFactory.initElements(driver,this);
        url=properties.getProperty("login.url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    @FindBy(id ="email")
    private WebElement userNameBox;

    @FindBy(id = "password")
    private WebElement passwordBox;

    @FindBy(xpath = "//div[@class='form-group button-holder']")
    private WebElement loginButton;


    // Launch the url to view the Landing page
    public void openLandingPage(){
        WebDriverHandler.launchApplication(String.valueOf(20));
        String URL = properties.getProperty("login.url");
        driver.get(URL);
        WebDriverHandler.waitForPage();

    }

    // Login to the launched url with username and password
    public void performLogin() {
        openLandingPage();
        // Passing the username and password from property file
        userNameBox.sendKeys(username);
        passwordBox.sendKeys(password);

        // Created node for the login page to log the screenshot and data info
        ExtentTest dataLog = ExtentTestManager.getTest().createNode("LoginPage");
        ExtentTestManager.logScreenShot(dataLog,WebDriverHandler.takeScreenShot(driver));
        dataLog.info("Login page open success");
        loginButton.click();
    }
}
