package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test measures page load time and checks if it meets performance thresholds.
 */
public class PageLoadPerformanceTest extends BaseTest {

    @Test(description = "Verify that page load time is within acceptable limit")
    public void testPageLoadPerformance() {
        long startTime = System.currentTimeMillis();

        driver.get("https://demo.opencart.com/");
        test.info("Navigated to OpenCart demo site");

        long endTime = System.currentTimeMillis();

        long loadTimeInMs = endTime - startTime;
        double loadTimeInSeconds = loadTimeInMs / 1000.0;

        test.info("⏱️ Page loaded in " + loadTimeInSeconds + " seconds");

        // Performance threshold (set your own limit)
        double threshold = 5.0;

        if (loadTimeInSeconds <= threshold) {
            test.pass("✅ Page loaded within threshold: " + loadTimeInSeconds + " seconds");
        } else {
            test.fail("❌ Page took too long to load: " + loadTimeInSeconds + " seconds");
        }

        // Optional assert (will fail test if too slow)
        Assert.assertTrue(loadTimeInSeconds <= threshold, "Page load time exceeded threshold");
    }
}
