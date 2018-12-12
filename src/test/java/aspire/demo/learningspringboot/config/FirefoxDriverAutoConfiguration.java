package aspire.demo.learningspringboot.config;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by andy.lv
 * on: 2018/12/12 15:42
 */
@Configuration
public class FirefoxDriverAutoConfiguration {

    @Bean
    public WebDriverAutoConfiguration.FirefoxDriverFactory firefoxDriverFactory(WebDriverConfigurationProperties properties) {
        return new WebDriverAutoConfiguration.FirefoxDriverFactory(properties);
    }

    @Bean
    public FirefoxDriver firefoxDriver(WebDriverAutoConfiguration.FirefoxDriverFactory firefoxDriverFactory) {
        return firefoxDriverFactory.getObject();
    }

}
