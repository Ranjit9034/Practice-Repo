package tests;

import base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Logs into Naukri and searches for matching jobs based on keywords and filters.
 */
public class NaukariJobSearchTest extends BaseTest {

    WebDriverWait wait;
    List<String> keywords = Arrays.asList("selenium", "testng", "cucumber", "api", "playwright");

    @Test(description = "Login to Naukri, search jobs, and match keywords in job descriptions")
    public void loginAndSearchJobs() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // STEP 1: Login to Naukri
        driver.get("https://www.naukri.com/nlogin/login");
        test.info("Opened Naukri login page");

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameField")));
        emailField.clear();
        emailField.sendKeys(prop.getProperty("naukri.email"));

        WebElement passwordField = driver.findElement(By.id("passwordField"));
        passwordField.clear();
        passwordField.sendKeys(prop.getProperty("naukri.pass"));

        WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        loginBtn.click();
        test.info("Submitted login credentials");

        // STEP 2: Wait for manual OTP if required
        Thread.sleep(15000); // Manually enter OTP if prompted

        // STEP 3: Click on "Search Jobs"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[@class='ni-gnb-icn ni-gnb-icn-search']"))).click();
        test.info("Clicked on Search Jobs");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());
        // STEP 4: Enter job title and location
        WebElement skillBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@placeholder='Enter keyword / designation / companies']")));
        skillBox.clear();
        skillBox.sendKeys("Software Testing");

        WebElement locationBox = driver.findElement(By.name("ql"));
        locationBox.clear();
        locationBox.sendKeys("India");
        locationBox.sendKeys(Keys.ENTER);

        // STEP 5: Apply filters
        Thread.sleep(3000);
        clickFilterByLabel("3-6 Lakhs"); // Experience
        clickFilterByLabel("Last 1 day"); // Freshness
        test.info("Applied filters: 3 Years, Last 1 Day");

        Thread.sleep(3000);

        // STEP 6: Loop through top 5 jobs
        List<WebElement> jobCards = driver.findElements(By.cssSelector("article.jobTuple"));
        int matched = 0;

        for (int i = 0; i < Math.min(jobCards.size(), 5); i++) {
            try {
                WebElement title = jobCards.get(i).findElement(By.cssSelector("a.title"));
                String jobTitle = title.getText();
                String jobLink = title.getAttribute("href");

                // Open job in new tab
                ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])", jobLink);
                Thread.sleep(2000);

                List<String> tabs = driver.getWindowHandles().stream().toList();
                driver.switchTo().window(tabs.get(1));
                Thread.sleep(2000);

                String jobDesc = driver.findElement(By.tagName("body")).getText().toLowerCase();
                boolean matchFound = keywords.stream().anyMatch(jobDesc::contains);

                if (matchFound) {
                    test.pass("✅ Matched: " + jobTitle + " - " + jobLink);
                    matched++;
                } else {
                    test.info("❌ Not matched: " + jobTitle);
                }

                driver.close();
                driver.switchTo().window(tabs.get(0));
                Thread.sleep(1000);

            } catch (Exception e) {
                test.warning("Error while checking job #" + (i + 1) + ": " + e.getMessage());
            }
        }

        test.info("Total matched jobs: " + matched);
    }

    private void clickFilterByLabel(String label) {
        try {
            WebElement checkbox = driver.findElement(By.xpath("//label[contains(text(),'" + label + "')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        } catch (Exception e) {
            test.warning("Filter not found: " + label);
        }
    }
}
