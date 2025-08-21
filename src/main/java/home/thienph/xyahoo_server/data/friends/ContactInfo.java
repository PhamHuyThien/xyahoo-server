package home.thienph.xyahoo_server.data.friends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactInfo {
    public String contactId;
    public String displayName;
    public int statusCode; //mã trạng thái (0=offline, 1=online, 2=busy...), 95%
    public String statusMessage;
    public boolean isOnline;
    public int[] permissions; //mảng quyền hạn (int[]), 85%
    public int roomId;
    public int userLevel;
    public int additionalFlags;
    public Integer colorObject;
}