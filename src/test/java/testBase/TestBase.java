package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class TestBase {
 //   public static WebDriver driver; // Use only one WebDriver instance
	
	 private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	 
	 public static WebDriver getDriver() {
	        return driver.get();
	    }
    public Logger logger;
    public Properties p;

    @SuppressWarnings("deprecation")
	@BeforeClass(groups = { "sanity", "master" })
    @Parameters({ "browser", "os" })
    public void setup(String br, String os) throws IOException {

        logger = LogManager.getLogger(this.getClass());

        logger.info("Starting setup process...");
        logger.info("Browser: " + br);

        try {
            logger.info("Loading configuration properties...");
            FileInputStream file = new FileInputStream(".//src//test//resources//config.properties");
            p = new Properties();
            p.load(file);
            logger.info("Configuration properties loaded successfully.");
        } catch (IOException e) {
            logger.error("Failed to load configuration properties.", e);
            throw e;
        }

        if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // os setup
            if (os.equalsIgnoreCase("windows")) {
                capabilities.setPlatform(Platform.WIN11);
                logger.info("Operating system set to Windows 11");
            } else if (os.equalsIgnoreCase("mac")) {
                capabilities.setPlatform(Platform.MAC);
                logger.info("Operating system set to Mac");
            } else {
                logger.error("No matching OS found: {}", os);
                return;
            }

            // browser setup
            switch (br.toLowerCase()) {
            case "chrome":
                capabilities.setBrowserName("chrome");
                logger.info("Browser set to Chrome");
                break;
            case "edge":
                capabilities.setBrowserName("MicrosoftEdge");
                logger.info("Browser set to Edge");
                break;
            default:
                logger.error("No matching browser found: {}", br);
                return;
            }

            try {
               // driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
            	driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
            	logger.info("RemoteWebDriver initialized with URL: http://localhost:4444/wd/hub");
            } catch (MalformedURLException e) {
                logger.error("Malformed URL for RemoteWebDriver", e);
            }
        } else {
            logger.warn("Execution environment is not set to remote.");
        }

        if (p.getProperty("execution_env").equalsIgnoreCase("local")) {
            logger.info("Initializing WebDriver for browser: " + br);
            switch (br.toLowerCase()) {
            case "chrome":
            	driver.set(new ChromeDriver());
                break;
            case "edge":
            	driver.set(new ChromeDriver());
                break;
            case "firefox":
            	driver.set(new ChromeDriver());
                break;
            default:
                logger.error("Invalid Browser: " + br);
                return;
            }
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(p.getProperty("appURL"));
        getDriver().manage().window().maximize();
    }

    public String randomeString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomeNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        String str = RandomStringUtils.randomAlphabetic(3);
        String num = RandomStringUtils.randomNumeric(3);
        return str + "@" + num;
    }

    public String captureScreen(String tname) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);
        sourceFile.renameTo(targetFile);
        return targetFilePath;
    }

    @AfterClass(groups = { "sanity", "master" })
    public void tearDown() {
        if (getDriver() != null) {
        	getDriver().quit();
            logger.info("WebDriver instance quit successfully.");
        } else {
            logger.warn("WebDriver instance is already null.");
        }
    }
}
