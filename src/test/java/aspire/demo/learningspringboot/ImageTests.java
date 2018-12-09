package aspire.demo.learningspringboot;

import aspire.demo.learningspringboot.image.Image;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTests {

    @Test
    public void imageManagedByLombokShouldWork() {
        Image image = new Image("id", "file-name.jpg");
        assertThat(image.getId()).isEqualTo("id");
        assertThat(image.getName()).isEqualTo("file-name.jpg");
    }
}
