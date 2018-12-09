package aspire.demo.learningspringboot;

import aspire.demo.learningspringboot.image.Image;
import aspire.demo.learningspringboot.image.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MongoOperations operations;

    @Before
    public void setUp() {
        operations.dropCollection(Image.class);
        operations.insertAll(Arrays.asList(
                new Image("1", "learning-spring-boot-cover.jpg"),
                new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"),
                new Image("3", "bazinga.png")
        ));
        operations.findAll(Image.class).forEach(System.out::println);
    }

    @Test
    public void findAllShouldWork() {
        Flux<Image> images = imageRepository.findAll();
        StepVerifier.create(images)
                .recordWith(ArrayList::new)
                .expectNextCount(3)
                .consumeRecordedWith(results -> {
                    assertThat(results).hasSize(3);

                    assertThat(results)
                            .extracting(Image::getName)
                            .contains(
                                    "learning-spring-boot-cover.jpg",
                                    "learning-spring-boot-2nd-edition.cover.jpg",
                                    "bazinga.png"
                            )
                    ;

                })
                .expectComplete()
                .verify();
    }

}
