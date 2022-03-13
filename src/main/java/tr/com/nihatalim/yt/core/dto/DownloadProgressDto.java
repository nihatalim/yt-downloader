package tr.com.nihatalim.yt.core.dto;

import lombok.Getter;
import lombok.Setter;
import tr.com.nihatalim.yt.core.enums.ContentType;
import tr.com.nihatalim.yt.core.enums.ProgressStatus;

@Getter
@Setter
public class DownloadProgressDto {
    private Long downloadProgressId;
    private String userId;
    private Long contentId;
    private Long contentDetailId;
    private String contentName;
    private ContentType contentType;
    private String extension;
    private String youtubeUrl;
    private String storageUrl;
    private String storageExtension;
    private ProgressStatus progressStatus;
}
