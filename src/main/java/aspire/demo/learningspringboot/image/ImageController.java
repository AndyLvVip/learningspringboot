package aspire.demo.learningspringboot.image;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

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

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.findAllImages());
        return Mono.just("index");
    }

    @PostMapping("/images")
    public Mono<Void> createImage(@RequestBody Flux<FilePart> files) {
        return imageService.createImage(files);
    }


    @GetMapping(value = "/images/{filename:.+}/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename) {
        return imageService.findOneImage(filename).map(resource -> {
            try {
                return ResponseEntity.ok().contentLength(resource.contentLength())
                        .body(new InputStreamResource(resource.getInputStream()));
            } catch (IOException e) {
                return ResponseEntity.badRequest()
                        .body("Couldn't find " + filename + " => " + e.getMessage());
            }
        });
    }
}
