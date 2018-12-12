package aspire.demo.learningspringboot;

import aspire.demo.learningspringboot.config.WebDriverAutoConfiguration;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ClassUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by andy.lv
 * on: 2018/12/12 13:32
 */
public class WebDriverAutoConfigurationTests {

    private AnnotationConfigApplicationContext applicationContext;

    @After
    public void close() {
        if(null != applicationContext)
            applicationContext.close();
    }


    private void load(Class<?>[] configs, String... environment) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(WebDriverAutoConfiguration.class);

        if(configs.length > 0)
            context.register(configs);


        TestPropertyValues.of(environment).applyTo(context);
        context.refresh();

        applicationContext = context;
    }

    @Test
    public void fallBackToNonGuiModeWhenAllBrowsersDisabled() {
        load(new Class<?>[]{},
                "aspire.demo.webdriver.firefox.enabled:false",
                "aspire.demo.webdriver.safari.enabled:false",
                "aspire.demo.webdriver.chrome.enabled:false"
        );

        WebDriver driver = applicationContext.getBean(WebDriver.class);
        assertThat(ClassUtils.isAssignable(TakesScreenshot.class, driver.getClass())).isFalse();
        assertThat(ClassUtils.isAssignable(HtmlUnitDriver.class, driver.getClass()));
    }

    @Test
    public void testWithMockedFirefox() {
        load(new Class<?>[]{}, "aspire.demo.webdriver.safari.enabled:false",
                "aspire.demo.webdriver.chrome.enabled:false");

        WebDriver webDriver = applicationContext.getBean(WebDriver.class);
        assertThat(ClassUtils.isAssignable(TakesScreenshot.class, webDriver.getClass())).isTrue();
        assertThat(ClassUtils.isAssignable(FirefoxDriver.class, webDriver.getClass())).isTrue();
    }
}
