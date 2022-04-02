package tr.com.nihatalim.yt.downloader.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${app.storage.bucket}")
    private String storageBucket;

    private final MinioClient minioClient;

    public StorageInitializer(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        makeSureBucketExists();
    }

    @SneakyThrows
    private void makeSureBucketExists() {
        final boolean bucketExists = minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(storageBucket).build()
        );

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(storageBucket).build());
        }
    }
}
