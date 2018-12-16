package aspire.demo.learningspringboot.comment;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;

public interface CommentWriterRepository extends Repository<Comment, String> {

    Mono<Comment> save(Comment newComment);

    Mono<Comment> findById(String id);

}
