package aspire.demo.learningspringboot;

import aspire.demo.learningspringboot.image.ImageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by andy.lv
 * on: 2018/12/10 13:08
 */

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@RunWith(SpringRunner.class)
public class LiveImageRepositoryTests extends ImageRepositoryTests {

    @Autowired
    private ImageRepository imageRepository;

    @Test
    public void findAllShouldWork() {
        super.findAllShouldWork(imageRepository);
    }

    @Test
    public void findByNameShouldWork() {
        super.findByNameShouldWork(imageRepository);
    }
}
