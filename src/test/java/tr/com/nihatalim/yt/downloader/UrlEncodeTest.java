package tr.com.nihatalim.yt.downloader;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

public class UrlEncodeTest {

    @Test
    public void test() {
        final String fileName = "9-Emir Can İğrek - Felfena (Official Video).mp3";
        final String encode = "http://minio:9000/public/" + UriUtils.encode(fileName, StandardCharsets.UTF_8);

    }
}
