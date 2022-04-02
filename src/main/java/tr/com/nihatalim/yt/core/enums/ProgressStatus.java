package tr.com.nihatalim.yt.core.enums;

public enum ProgressStatus {
    DOWNLOADING(1),
    DOWNLOAD_FAILED(-1),
    CONVERTING(2),
    CONVERT_FAILED(-2),
    COMPLETED(0);

    private final int value;

    ProgressStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
