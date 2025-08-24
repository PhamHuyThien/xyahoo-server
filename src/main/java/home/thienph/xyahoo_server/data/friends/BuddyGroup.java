package home.thienph.xyahoo_server.data.friends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class BuddyGroup {
    public List<BuddyInfo> contacts;
    private String groupName;
    public int expansionStatus;// (0=collapsed, 1=expanded), 90%
// (0=collapsed, 1=expanded), 90%

}
