package aspire.demo.learningspringboot.image;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by andy.lv
 * on: 2018/11/30 17:01
 */
@Service
public class ImageService {

    public static String UPLOAD_ROOT = "upload-dir";

    private final ImageRepository imageRepository;

    private final ResourceLoader resourceLoader;

    public ImageService(ImageRepository imageRepository, ResourceLoader resourceLoader) {
        this.imageRepository = imageRepository;
        this.resourceLoader = resourceLoader;
    }

    public Flux<Image> findAllImages() {
        return imageRepository.findAll();
    }

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files.flatMap(file -> {
            Mono<Image> saveDatabaseImage = imageRepository.save(new Image(UUID.randomUUID().toString(), file.filename()));

            Mono<Void> copyFile = Mono.just(
                    Paths.get(UPLOAD_ROOT, file.filename()).toFile()
            ).log("createImage-picktarget")
                    .map(destFile -> {
                        try {
                            destFile.createNewFile();
                            return destFile;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .log("createImage-newfile")
                    .flatMap(destFile -> file.transferTo(destFile))
                    .log("createImage-copy");
            return Mono.when(saveDatabaseImage, copyFile);
        }).then();
    }

    public Mono<Void> deleteImage(String filename) {
        Mono<Void> deleteImageAction = imageRepository.findByName().flatMap(imageRepository::delete);
        Mono<Void> deleteFileAction = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return Mono.when(deleteImageAction, deleteFileAction).then();
    }
}
