package commons;

import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.managers.EdgeDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WebDriverHandler {

    static Map<Integer, WebDriver> driverMap = new HashMap<>();
    public static final int DEFAULT_WAIT_TIME = 40;


    public static synchronized WebDriver CreateDriver() {
        driverMap.put((int) (Thread.currentThread().threadId()), InitializeDriver("Chrome"));
        return driverMap.get((int) (Thread.currentThread().threadId()));
    }

    public static synchronized WebDriver getDriver() {
        return driverMap.get((int) (Thread.currentThread().threadId()));
    }

    public static void quitDriver() {
        try {
            driverMap.get((int) (Thread.currentThread().threadId())).quit();
            driverMap.remove((int) (Thread.currentThread().threadId()));
        } catch (Exception e) {
            System.out.println("Driver not present for current Thread" + Thread.currentThread().threadId());
        }
    }


    public static WebDriver InitializeDriver(String browser) {
        WebDriver driver = null;
        try {
            if (browser.equalsIgnoreCase("FF") || browser.equalsIgnoreCase("Firefox")) {
                FirefoxDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
                driver = new FirefoxDriver();
            } else if (browser.equalsIgnoreCase("Chrome")) {
                ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.setPageLoadStrategy(PageLoadStrategy.NONE);
                driver = new ChromeDriver(options);
            } else if (browser.equalsIgnoreCase("IE") || browser.equalsIgnoreCase("Internet Explorer")) {
                EdgeDriverManager.getInstance(DriverManagerType.EDGE).setup();
                driver = new EdgeDriver();
            }
            driver.manage().window().maximize();
        } catch (Exception e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status.FAIL, "Exception occured while invoking Local driver</br>" + e.getMessage());
        }
        return driver;
    }


    public static void launchApplication(String browser) {
        try {
            getDriver().get(browser);
            waitForPage();
            ExtentTestManager.getTest().log(Status.INFO, "Launched URL - " + browser);
        } catch (Exception e) {

        }
    }


    public static void waitForPage() {
        try {
            WebDriver driver = getDriver();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME);
            final JavascriptExecutor executor = (JavascriptExecutor) driver;
            ExpectedCondition<Boolean> condition = arg0 -> executor.executeScript("return document.readyState").equals("complete");

            wait.until(condition);
        } catch (TimeoutException e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Page not loaded within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Element not found within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Page not loaded within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
        }
    }


    public static void waitTillVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
        wait.until(ExpectedConditions.visibilityOfAllElements(element));
    }


    public static String takeScreenShot(WebDriver driver) {
        try {
            TakesScreenshot newScreen = (TakesScreenshot) driver;
            String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
            return "data:image/jpg;base64, " + scnShot;
        } catch (Exception e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status.FAIL, "Unable to Take Screenshot!!</br>" + e.getMessage());
            return "";
        }
    }

    public static void waitForElement(WebElement element, int timeoutInSeconds) {
        Wait wait;
        FluentWait fluentWait = new FluentWait(getDriver());
        fluentWait.withTimeout(Duration.ofSeconds(timeoutInSeconds));
        fluentWait.pollingEvery(Duration.ofSeconds(250));
        fluentWait.ignoring(Exception.class);
        wait = fluentWait;
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}