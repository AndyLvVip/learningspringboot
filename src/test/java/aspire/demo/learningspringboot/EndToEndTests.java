package aspire.demo.learningspringboot;

import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private DriverService driverService;
    @Autowired
    private WebDriver webDriver;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws IOException {
        Path testResults = Paths.get("build", "test-results");
        if(!Files.exists(testResults)) {
            Files.createDirectories(testResults);
        }
    }

    @After
    public void tearDown() {
        if(null != driverService)
            driverService.stop();
    }

    private void takeScreenShot(String name) throws IOException {
        if(webDriver instanceof TakesScreenshot)
            FileCopyUtils.copy(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE),
                    new File("build/test-results/TEST-" + name + ".png"));
    }

    @Test
    public void homePageShouldWork() throws IOException {
        webDriver.get("http://localhost:" + port);
        takeScreenShot("homePageShouldWork-1");

        assertThat(webDriver.getTitle()).isEqualTo("Learning Spring Boot: Spring-a-Gram");

        String pageContent = webDriver.getPageSource();
        System.out.println(pageContent);
        assertThat(pageContent).contains("<a href=\"/images/bazinga.png/raw\">");

        WebElement webElement = webDriver.findElement(By.cssSelector("a[href*=\"bazinga.png\"]"));

        Actions actions = new Actions(webDriver);

        actions.moveToElement(webElement).click().perform();

        takeScreenShot("homePageShouldWork-2");
        webDriver.navigate().back();
        takeScreenShot("homePageShouldWork-3");
    }

}
