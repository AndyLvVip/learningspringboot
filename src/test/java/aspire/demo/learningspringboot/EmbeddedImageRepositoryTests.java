package aspire.demo.learningspringboot;

import aspire.demo.learningspringboot.image.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests extends ImageRepositoryTests{

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MongoOperations operations;

    @Before
    public void setup() {
        super.setUp(operations);
    }

    @Test
    public void findAllShouldWork() {
        super.findAllShouldWork(imageRepository);
    }

    @Test
    public void findByNameShouldWork() {
        super.findByNameShouldWork(imageRepository);
    }
}
