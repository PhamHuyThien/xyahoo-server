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
    public int mediaType;
    public String description;
    public boolean isSelected;
    public String mediaExtension;
    private int[] rawDataArray;
    public Integer[] processedDataArray;
    public byte[] mediaData;
    public int dataSize;
    public long contactId;
    public Integer imageSourceId;
    private final int defaultColor = 16777215;
    public byte[] thumbnailImage;

    public BuddyInfo(String groupName, String displayName, String mediaExtension, String description, Integer imageSourceId, byte[] mediaData, int statusCode, String statusDescription) {
        this.groupName = groupName;
        this.displayName = displayName;
        this.mediaExtension = mediaExtension;
        this.description = description;
        this.imageSourceId = imageSourceId;
        this.mediaData = mediaData;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }


    public BuddyInfo(String description, String username, long contactId) {
        this.description = description;
        this.username = username;
        this.contactId = contactId;
    }


}