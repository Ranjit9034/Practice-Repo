package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * This test checks cookie-based session handling.
 * It logs in, reads cookies, deletes them, and verifies session expiration.
 */
public class SessionAndCookiesTest extends BaseTest {

    @Test(description = "Validate session handling using cookies")
    public void testSessionViaCookies() throws InterruptedException {

        // STEP 1: Navigate to login page
        driver.get("https://demo.guru99.com/test/newtours/login.php"); // ✅ using a login-ready site
        test.info("Opened login page");

        // STEP 2: Login with valid credentials
        driver.findElement(By.name("userName")).sendKeys("tutorial");
        driver.findElement(By.name("password")).sendKeys("tutorial");
        driver.findElement(By.name("submit")).click();
        test.info("Logged in with valid credentials");

        Thread.sleep(2000); // let it load

        // STEP 3: Capture and log all cookies
        Set<Cookie> cookies = driver.manage().getCookies();
        test.info("Captured cookies after login:");
        for (Cookie cookie : cookies) {
            test.info("🍪 " + cookie.getName() + " = " + cookie.getValue());
        }

        // STEP 4: Delete cookies (simulate logout)
        driver.manage().deleteAllCookies();
        test.info("Deleted all cookies to simulate logout");

        // STEP 5: Refresh page to test session status
        driver.navigate().refresh();
        Thread.sleep(2000);

        // STEP 6: Check if user is still logged in or redirected
        boolean isLoggedOut = driver.getPageSource().contains("User or Password is not valid");

        if (isLoggedOut) {
            test.pass("✅ Session expired as expected after deleting cookies.");
        } else {
            test.fail("❌ User is still logged in — session did not expire.");
        }

        Assert.assertTrue(isLoggedOut, "User should be logged out after deleting cookies.");
    }
}
