package aspire.demo.learningspringboot.image;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

/**
 * Created by andy.lv
 * on: 2018/12/6 13:56
 */
@Controller
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.findAllImages());
        return Mono.just("index");
    }
}
