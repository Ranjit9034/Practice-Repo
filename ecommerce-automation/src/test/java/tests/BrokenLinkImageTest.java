package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * This test verifies that there are no broken links or images on the page.
 */
public class BrokenLinkImageTest extends BaseTest {

    @Test(description = "Verify that all links and images are not broken")
    public void checkBrokenLinksAndImages() {
      //  driver.get(prop.getProperty("url"));
       // test.info("Navigated to URL: " + prop.getProperty("url"));

        // Collect all <a> and <img> elements
        List<WebElement> elements = driver.findElements(By.xpath("//a[@href] | //img"));
        test.info("Total links and images found: " + elements.size());

        Assert.assertTrue(elements.size() > 0, "No links or images found on the page");

        // Validate each link or image
        for (WebElement element : elements) {
            String url = element.getAttribute("href");

            // If it's an image, use src instead
            if (url == null || url.isEmpty()) {
                if (element.getTagName().equals("img")) {
                    url = element.getAttribute("src");
                }
            }

            // If still empty, skip
            if (url == null || url.isEmpty()) {
                test.warning("URL is empty for element: <" + element.getTagName() + ">");
                continue;
            }

            boolean isValid = verifyLink(url);
            Assert.assertTrue(isValid, "Broken link/image found: " + url);
        }

        test.pass("All links and images are valid.");
    }

    /**
     * Helper method to verify URL response
     */
    public boolean verifyLink(String linkUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(linkUrl).openConnection());
            connection.setRequestMethod("HEAD");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode >= 400) {
                test.fail(linkUrl + " is broken. Status code: " + responseCode);
                return false;
            } else {
                test.info(linkUrl + " is valid. Status code: " + responseCode);
                return true;
            }
        } catch (Exception e) {
            test.fail(linkUrl + " caused exception: " + e.getMessage());
            return false;
        }
    }
}
