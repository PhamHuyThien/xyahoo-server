package home.thienph.xyahoo_server.data.friends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class BuddyInfo {
    public String groupName;
    public String username;
    public String displayName;
    public int statusCode; //0 xanh, 1 vang, 2 do
    public String statusDescription;
    public int downloadType;
    public String description;
    public boolean isSelected;
    public String fileName;
    private int[] rawDataArray;

    public BuddyInfo(String groupName, String displayName, String fileName, String description, Integer imageSourceId, byte[] imageBytes, int statusCode, String statusDescription) {
        this.groupName = groupName;
        this.displayName = displayName;
        this.fileName = fileName;
        this.description = description;
        this.imageSourceId = imageSourceId;
        this.imageBytes = imageBytes;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

    public Integer[] processedDataArray;
    public byte[] imageBytes;
    public int dataSize;
    public long timestamp;
    public Integer imageSourceId;
    private final int defaultColor = 16777215;
    public byte[] thumbnailImage;
}