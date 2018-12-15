package aspire.demo.learningspringboot.indicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class LearningSpringBootHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try {
            URL url = new URL("http://localhost:9000/foo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseStatus = conn.getResponseCode();
            if(responseStatus >= 200 && responseStatus < 300) {
                return Health
                        .up()
                        .build()
                        ;
            }else {
                return Health.down().withDetail("Http Status Code", responseStatus)
                        .build();
            }

        } catch (IOException e) {
            return Health.down(e)
                    .build();
        }
    }
}
