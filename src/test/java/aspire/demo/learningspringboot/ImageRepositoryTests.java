package aspire.demo.learningspringboot;

import aspire.demo.learningspringboot.image.Image;
import aspire.demo.learningspringboot.image.ImageRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by andy.lv
 * on: 2018/12/10 13:09
 */
public abstract class ImageRepositoryTests {

    protected void setUp(MongoOperations operations) {
        operations.dropCollection(Image.class);
        operations.insertAll(Arrays.asList(
                new Image("1", "learning-spring-boot-cover.jpg"),
                new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"),
                new Image("3", "bazinga.png")
        ));
        operations.findAll(Image.class).forEach(System.out::println);
    }

    protected void findAllShouldWork(ImageRepository imageRepository) {
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
                                    "learning-spring-boot-2nd-edition-cover.jpg",
                                    "bazinga.png"
                            )
                    ;

                })
                .expectComplete()
                .verify();
    }

    protected void findByNameShouldWork(ImageRepository imageRepository) {
        Mono<Image> image = imageRepository.findByName("bazinga.png");
        StepVerifier.create(image)
                .expectNextMatches(results -> {
                    assertThat(results.getName()).isEqualTo("bazinga.png");
                    assertThat(results.getId()).isEqualTo("3");
                    return true;
                });
    }
}
