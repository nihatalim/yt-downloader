package tr.com.nihatalim.yt.downloader.util;

public class ApplicationUtil {
    public static int getConcurrencyLevel() {
        return Runtime.getRuntime().availableProcessors() * 2;
    }
}
