package tr.com.nihatalim.yt.core.enums;

import java.util.Arrays;

public enum ContentType {
    AUDIO("AUDIO"),
    VIDEO("VIDEO");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ContentType fromString(String value) {
        return Arrays.stream(ContentType.values())
            .filter(item -> item.getValue().equals(value))
            .findFirst()
            .orElse(null);
    }
}
