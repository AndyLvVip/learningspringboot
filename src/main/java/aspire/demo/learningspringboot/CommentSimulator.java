package aspire.demo.learningspringboot;

import aspire.demo.learningspringboot.comment.Comment;
import aspire.demo.learningspringboot.comment.CommentController;
import aspire.demo.learningspringboot.image.ImageRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Profile("simulator")
public class CommentSimulator {

    private final CommentController commentController;

    private final AtomicInteger counter;

    private final ImageRepository repository;

    public CommentSimulator(CommentController commentController,
                            ImageRepository repository) {
        this.commentController = commentController;
        this.counter = new AtomicInteger(1);
        this.repository = repository;
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {

        Flux.interval(Duration.ofSeconds(1))
                .flatMap(tick -> repository.findAll())
                .map(image -> {
                    Comment comment = new Comment();
                    comment.setImageId(image.getId());
                    comment.setComment("Comment #" + counter.getAndIncrement());
                    return Mono.just(comment);
                }).flatMap(comment -> Mono.defer(() -> commentController.addComment(comment)))
                .subscribe()
        ;
    }
}
