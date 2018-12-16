package aspire.demo.learningspringboot.comment;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentWriterRepository repository;
    private final MeterRegistry meterRegistry;

    public CommentService(CommentWriterRepository repository,
                          MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "learning-spring-boot"),
                    key = "comment.new"
            )
    )
    public void save(Comment newComment) {
        repository.save(newComment)
                .log("commentService-save")
                .subscribe(comment -> meterRegistry.counter("comment.consumed", "imageId", comment.getImageId())
                        .increment()
                );
    }
}
