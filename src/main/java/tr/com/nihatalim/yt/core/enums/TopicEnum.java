package tr.com.nihatalim.yt.core.enums;

public enum TopicEnum {
    DOWNLOAD_EVENT("DOWNLOAD_EVENT", 4),
    DOWNLOAD_FAILED_EVENT("DOWNLOAD_FAILED_EVENT", 4),
    DOWNLOAD_COMPLETED_EVENT("DOWNLOAD_COMPLETED_EVENT", 4),
    CONVERT_EVENT("CONVERT_EVENT", 4),
    CONVERT_FAILED_EVENT("CONVERT_FAILED_EVENT", 4),
    CONVERT_COMPLETED_EVENT("CONVERT_COMPLETED_EVENT", 4),
    DOWNLOAD_PROGRESS_COMPLETED_EVENT("DOWNLOAD_PROGRESS_COMPLETED_EVENT", 4);

    private final String topicName;
    private final int partitionNumber;

    TopicEnum(String topicName, int partitionNumber) {
        this.topicName = topicName;
        this.partitionNumber = partitionNumber;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getPartitionNumber() {
        return partitionNumber;
    }
}
