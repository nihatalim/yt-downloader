package tr.com.nihatalim.yt.downloader.util;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class YoutubeUrlUtil {
    private YoutubeUrlUtil() {

    }

    public static String getYoutubeIdFromYoutubeUrl(String youtubeUrl) {
        final MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromHttpUrl(youtubeUrl).build().getQueryParams();
        return queryParams.getFirst("v");
    }

    public static String getContentName(String fileName) {
        return fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("."));
    }
}
