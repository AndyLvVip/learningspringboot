package aspire.demo.learningspringboot;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by andy.lv
 * on: 2018/12/11 15:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTests {

    private static ChromeDriverService chromeDriverService;
    private static ChromeDriver chromeDriver;

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void setup() throws IOException {
        System.setProperty("webdriver.chrome.driver", "ext/chromedriver.exe");

        chromeDriverService = ChromeDriverService.createDefaultService();
        chromeDriver = new ChromeDriver(chromeDriverService);

        Path testResults = Paths.get("build", "test-results");
        if(!Files.exists(testResults)) {
            Files.createDirectories(testResults);
        }
    }

    @AfterClass
    public static void tearDown() {
        chromeDriverService.stop();
    }

    private void takeScreenShot(String name) throws IOException {
        FileCopyUtils.copy(chromeDriver.getScreenshotAs(OutputType.FILE),
                new File("build/test-results/TEST-" + name + ".png"));
    }

    @Test
    public void homePageShouldWork() throws IOException {
        chromeDriver.get("http://localhost:" + port);
        takeScreenShot("homePageShouldWork-1");

        assertThat(chromeDriver.getTitle()).isEqualTo("Learning Spring Boot: Spring-a-Gram");

        String pageContent = chromeDriver.getPageSource();
        System.out.println(pageContent);
        assertThat(pageContent).contains("<a href=\"/images/bazinga.png/raw\">");

        WebElement webElement = chromeDriver.findElement(By.cssSelector("a[href*=\"bazinga.png\"]"));

        Actions actions = new Actions(chromeDriver);

        actions.moveToElement(webElement).click().perform();

        takeScreenShot("homePageShouldWork-2");
        chromeDriver.navigate().back();
        takeScreenShot("homePageShouldWork-3");
    }

}
