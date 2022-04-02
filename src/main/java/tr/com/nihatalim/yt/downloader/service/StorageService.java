package tr.com.nihatalim.yt.downloader.service;

import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StorageService {
    private final MinioClient minioClient;

    @Value("${app.storage.bucket}")
    private String bucket;

    public StorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @SneakyThrows
    public void store(String downloadedFileName) {
        minioClient.uploadObject(
            UploadObjectArgs.builder()
                .bucket(bucket)
                .object(downloadedFileName)
                .filename(downloadedFileName)
                .build()
        );
    }
}
