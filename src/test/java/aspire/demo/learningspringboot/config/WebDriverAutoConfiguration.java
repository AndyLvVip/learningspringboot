package aspire.demo.learningspringboot.config;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * Created by andy.lv
 * on: 2018/12/12 14:09
 */
@Configuration
@ConditionalOnClass(WebDriver.class)
@EnableConfigurationProperties(WebDriverConfigurationProperties.class)
@Import({WebDriverAutoConfiguration.FirefoxDriverFactory.class,
        WebDriverAutoConfiguration.SafariDriverFactory.class,
        WebDriverAutoConfiguration.ChromeDriverFactory.class
})
public class WebDriverAutoConfiguration {

    @Primary
    @Bean(destroyMethod = "quit")
    @ConditionalOnMissingBean(WebDriver.class)
    public WebDriver webDriver(FirefoxDriverFactory firefoxDriverFactory,
                               SafariDriverFactory safariDriverFactory,
                               ChromeDriverFactory chromeDriverFactory) {
        WebDriver webDriver = firefoxDriverFactory.getObject();
        if(null == webDriver)
            webDriver = safariDriverFactory.getObject();
        if(null == webDriver)
            webDriver = chromeDriverFactory.getObject();
        if(null == webDriver)
            webDriver = new HtmlUnitDriver();
        return webDriver;
    }

    static class FirefoxDriverFactory implements ObjectFactory<FirefoxDriver> {

        private final WebDriverConfigurationProperties properties;

        public FirefoxDriverFactory(WebDriverConfigurationProperties properties) {
            this.properties = properties;
        }

        @Override
        public FirefoxDriver getObject() throws BeansException {
            if(properties.getFirefox().isEnabled()) {
                if(SystemUtils.IS_OS_WINDOWS)
                    System.setProperty("webdriver.gecko.driver", "ext/geckodriver.exe");
                else if(SystemUtils.IS_OS_LINUX)
                    System.setProperty("webdriver.gecko.driver", "ext/geckodriver");
                try {
                    return new FirefoxDriver();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    static class ChromeDriverFactory implements ObjectFactory<ChromeDriver> {

        private final WebDriverConfigurationProperties properties;

        public ChromeDriverFactory(WebDriverConfigurationProperties properties) {
            this.properties = properties;
        }

        @Override
        public ChromeDriver getObject() throws BeansException {
            if(properties.getChrome().isEnabled()) {
                try {
                    if(SystemUtils.IS_OS_WINDOWS)
                        System.setProperty("webdriver.chrome.driver", "ext/chromedriver.exe");
                    else if(SystemUtils.IS_OS_LINUX)
                        System.setProperty("webdriver.chrome.driver", "ext/chromedriver");
                    return new ChromeDriver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    static class SafariDriverFactory implements ObjectFactory<SafariDriver> {

        private final WebDriverConfigurationProperties properties;

        public SafariDriverFactory(WebDriverConfigurationProperties properties) {
            this.properties = properties;
        }

        @Override
        public SafariDriver getObject() throws BeansException {
            if(properties.getSafari().isEnabled()) {
                try {
                    return new SafariDriver();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

}
