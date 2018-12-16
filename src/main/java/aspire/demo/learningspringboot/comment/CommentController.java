package aspire.demo.learningspringboot.comment;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {
    private final RabbitTemplate rabbitTemplate;

    private final MeterRegistry meterRegistry;

    public CommentController(RabbitTemplate rabbitTemplate,
                             MeterRegistry meterRegistry) {
        this.rabbitTemplate = rabbitTemplate;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment.flatMap(
                comment -> Mono.fromRunnable(
                        () -> rabbitTemplate
                                .convertAndSend("learning-spring-boot"
                                        , "comment.new"
                                        , comment
                                )
                )
                        .then(Mono.just(comment))
                        .log("commentService-publish")
                        .flatMap(c -> {
                            meterRegistry.counter("comment.produced", "imageId", c.getImageId())
                                    .increment();
                            return Mono.just("redirect:/");
                        })

        );
    }
}
