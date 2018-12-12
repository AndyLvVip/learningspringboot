package aspire.demo.learningspringboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by andy.lv
 * on: 2018/12/12 14:16
 */
@Data
@ConfigurationProperties(prefix = "aspire.demo.webdriver")
public class WebDriverConfigurationProperties {

    private final Firefox firefox = new Firefox();

    private final Chrome chrome = new Chrome();

    private final Safari safari = new Safari();

    @Data
    static class Firefox {
        private boolean enabled = true;
    }

    @Data
    static class Safari{
        private boolean enabled = true;
    }

    @Data
    static class Chrome {
        private boolean enabled = true;
    }

}
