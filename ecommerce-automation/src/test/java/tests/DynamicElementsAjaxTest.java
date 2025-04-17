package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * This test handles dynamic loading using AJAX and waits for content to appear.
 */
public class DynamicElementsAjaxTest extends BaseTest {

    @Test(description = "Handle dynamic elements and AJAX loading")
    public void testDynamicAjaxLoading() {

        // STEP 1: Navigate to the test page
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
        test.info("Navigated to Dynamic Loading test page");

        // STEP 2: Click the Start button
        driver.findElement(By.cssSelector("#start button")).click();
        test.info("Clicked on Start button to trigger loading");

        // STEP 3: Wait for the result text to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));

        String resultText = driver.findElement(By.id("finish")).getText();
        test.info("Loaded text: " + resultText);

        // STEP 4: Assert the expected text
        Assert.assertEquals(resultText, "Hello World!", "Text did not load as expected");
        test.pass("✅ Dynamic text appeared as expected: " + resultText);
    }
}
