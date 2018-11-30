package aspire.demo.learningspringboot.image;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Created by andy.lv
 * on: 2018/11/30 16:24
 */
public interface ImageRepository extends ReactiveCrudRepository<Image, String> {

}
