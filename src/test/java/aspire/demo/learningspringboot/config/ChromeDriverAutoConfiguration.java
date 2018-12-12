package aspire.demo.learningspringboot.config;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by andy.lv
 * on: 2018/12/12 15:56
 */
@Configuration
public class ChromeDriverAutoConfiguration {
    @Bean
    public WebDriverAutoConfiguration.ChromeDriverFactory chromeDriverFactory(WebDriverConfigurationProperties properties) {
        return new WebDriverAutoConfiguration.ChromeDriverFactory(properties);
    }

    @Bean
    @Primary
    public ChromeDriver chromeDriver(WebDriverAutoConfiguration.ChromeDriverFactory chromeDriverFactory) {
        return chromeDriverFactory.getObject();
    }

}
