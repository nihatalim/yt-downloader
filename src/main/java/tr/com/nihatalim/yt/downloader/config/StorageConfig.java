package tr.com.nihatalim.yt.downloader.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${app.storage.endpoint}")
    private String storageEndpoint;

    @Value("${app.storage.accesskey}")
    private String storageAccessKey;

    @Value("${app.storage.secretkey}")
    private String storageSecretKey;

    @Bean
    public MinioClient minioClient () {
        return MinioClient.builder()
            .endpoint(storageEndpoint)
            .credentials(storageAccessKey, storageSecretKey)
            .build();
    }
}
