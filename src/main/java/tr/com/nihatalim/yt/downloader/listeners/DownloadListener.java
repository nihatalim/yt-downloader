package tr.com.nihatalim.yt.downloader.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;
import tr.com.nihatalim.yt.core.dto.DownloadProgressDto;
import tr.com.nihatalim.yt.core.enums.ProgressStatus;
import tr.com.nihatalim.yt.core.enums.TopicEnum;
import tr.com.nihatalim.yt.downloader.exception.YoutubeDownloaderException;
import tr.com.nihatalim.yt.downloader.service.DistributionService;
import tr.com.nihatalim.yt.downloader.service.StorageService;
import tr.com.nihatalim.yt.downloader.util.YoutubeUrlUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DownloadListener {

    @Value("${app.storage.public_endpoint}")
    private String storageEndpoint;

    @Value("${app.storage.bucket}")
    private String storageBucket;


    private final DistributionService distributionService;
    private final StorageService storageService;

    public DownloadListener(DistributionService distributionService, StorageService storageService) {
        this.distributionService = distributionService;
        this.storageService = storageService;
    }

    @KafkaListener(topics = "${app.kafka.topics.download-event}")
    @Transactional
    public void downloadCompleted(ConsumerRecord<Long, DownloadProgressDto> record) {
        final DownloadProgressDto downloadProgressDto = record.value();

        log.info("[DOWNLOAD_EVENT] Download starting for youtubeUrl: {} operated by user: {} requested for contentType: {} and extension: {}", downloadProgressDto.getYoutubeUrl(), downloadProgressDto.getUserId(), downloadProgressDto.getContentType().getValue(), downloadProgressDto.getExtension());

        try {
            final String downloadedFileName = downloadVideo(downloadProgressDto);
            log.info("[DOWNLOADED_FILE_NAME] {}", downloadedFileName);
            storageService.store(downloadedFileName);

            downloadProgressDto.setProgressStatus(ProgressStatus.COMPLETED);
            downloadProgressDto.setStorageUrl(getStorageUrl(downloadedFileName));
            downloadProgressDto.setStorageExtension(downloadProgressDto.getExtension());
            downloadProgressDto.setContentName(YoutubeUrlUtil.getContentName(downloadedFileName));

            removeDownloadedFile(downloadedFileName);

            log.info("[DOWNLOAD_EVENT] Download successful for youtubeUrl: {} operated by user: {} requested for contentType: {} and extension: {}", downloadProgressDto.getYoutubeUrl(), downloadProgressDto.getUserId(), downloadProgressDto.getContentType().getValue(), downloadProgressDto.getExtension());

        } catch (Exception e) {
            log.error("[DOWNLOAD_EVENT] Download failed for youtubeUrl: {} operated by user: {} requested for contentType: {} and extension: {}", downloadProgressDto.getYoutubeUrl(), downloadProgressDto.getUserId(), downloadProgressDto.getContentType().getValue(), downloadProgressDto.getExtension());
            downloadProgressDto.setProgressStatus(ProgressStatus.DOWNLOAD_FAILED);
        } finally {
            distributionService.send(TopicEnum.DOWNLOAD_COMPLETED_EVENT, downloadProgressDto);
        }
    }

    private String getStorageUrl(String downloadedFileName) {
        return String.format("%s/%s/%s", storageEndpoint, storageBucket, UriUtils.encode(downloadedFileName, StandardCharsets.UTF_8));
    }

    private String downloadVideo(DownloadProgressDto downloadProgressDto) {
        try {
            final List<String> commands = Arrays.asList(
                "youtube-dl",
                "-o",
                downloadProgressDto.getDownloadProgressId() + "-%(title)s.%(ext)s",
                downloadProgressDto.getYoutubeUrl(),
                "-x",
                "--audio-format",
                "mp3"
            );

            final Process start = new ProcessBuilder().command(commands).start();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));

            log.info("[DOWNLOAD_VIDEO_LOGS]");
            final List<String> outputs = bufferedReader.lines()
                .peek(log::info)
                .collect(Collectors.toList());

            log.info("[DOWNLOAD_VIDEO_LOGS]");

            return getDownloadedFileName(outputs);

        } catch (Exception e) {
            log.error("[DOWNLOAD_EVENT] Error while downloading video with url: {}", downloadProgressDto.getYoutubeUrl());
            throw new YoutubeDownloaderException("Download failed.");
        }
    }

    private void removeDownloadedFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
            log.info("[removeDownloadedFile] {} has been deleted!", fileName);
        } catch (Exception e) {
            log.error("[removeDownloadedFile] {} cannot be deleted!", fileName);
        }
    }

    private String getDownloadedFileName(List<String> output) {
        String fileName = getFileNameAlreadyExists(output);

        log.info("[getDownloadedFileName] (getFileNameAlreadyExists) fileName: {}", fileName);

        if (Objects.nonNull(fileName)) {
            return fileName;
        }

        final String expression = "Audio] Destination: ";

        return output.stream()
            .filter(item -> item.contains(expression))
            .findFirst()
            .map(item -> item.split(expression))
            .map(item -> item.length > 1 ? item[1] : null)
            .orElse(null);
    }

    private String getFileNameAlreadyExists(List<String> output) {
        final String expression = " has already been downloaded";
        final String prefix = "[download] ";

        return output.stream()
            .filter(item -> item.contains(expression))
            .findFirst()
            .map(item -> item.split(expression))
            .map(item -> item.length > 1 ? item[0] : null)

            .map(item -> item.split(prefix))
            .map(item -> item.length > 1 ? item[1] : null)
            .orElse(null);
    }
}
