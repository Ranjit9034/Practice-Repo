package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.ConfigReader;
import utils.ExtentManager;
import utils.ScreenshotUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Properties;

public class BaseTest {

    public static WebDriver driver;
    public static Properties prop;
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeSuite
    public void loadFramework() {
        // Load properties using utility
        prop = ConfigReader.initProperties();

        // Create screenshot folder if not exists
        File screenshotDir = new File("screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdir();
        }

        // Initialize report from ExtentManager
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        String browser = prop.getProperty("browser").toLowerCase();

        switch (browser) {
            case "chrome":
                driver = new ChromeDriver(); break;
            case "firefox":
                driver = new FirefoxDriver(); break;
            case "edge":
                driver = new EdgeDriver(); break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.get(prop.getProperty("url"));

        test = extent.createTest(method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
            if (screenshotPath != null) {
                test.fail("Test Failed: " + result.getThrowable());
                try {
                    test.addScreenCaptureFromPath(screenshotPath);
                } catch (Exception e) {
                    test.warning("Screenshot not attached: " + e.getMessage());
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test Skipped: " + result.getThrowable());
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
