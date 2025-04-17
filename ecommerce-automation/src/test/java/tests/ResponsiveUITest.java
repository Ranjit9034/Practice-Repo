package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test verifies that the application layout adapts correctly to different screen sizes.
 */
public class ResponsiveUITest extends BaseTest {

    @Test(description = "Verify layout responsiveness across screen sizes")
    public void verifyResponsiveUI() throws InterruptedException {
        test.info("Testing layout responsiveness for different viewports.");

        // ========================
        // 1. Desktop View Test
        // ========================
        driver.manage().window().setSize(new Dimension(1920, 1080));
        Thread.sleep(2000); // Allow layout to adjust

        WebElement desktopHeader = driver.findElement(By.id("nava")); // Demoblaze logo
        Assert.assertTrue(desktopHeader.isDisplayed(), "Logo should be visible on desktop view.");
        test.pass("Logo is visible on Desktop resolution.");

        // ========================
        // 2. Mobile View Test
        // ========================
        driver.manage().window().setSize(new Dimension(375, 667)); // iPhone-like screen
        Thread.sleep(2000);

        WebElement toggleMenu = driver.findElement(By.className("navbar-toggler"));
        Assert.assertTrue(toggleMenu.isDisplayed(), "Toggle menu should be visible on mobile view.");
        test.pass("Hamburger menu is visible on Mobile resolution.");

        // Click toggle and check nav menu appears
        toggleMenu.click();
        Thread.sleep(1000);
        WebElement contactLink = driver.findElement(By.linkText("Contact"));
        Assert.assertTrue(contactLink.isDisplayed(), "Contact link should be visible in mobile menu.");
        test.pass("Contact link is visible after expanding hamburger menu.");
    }
}
