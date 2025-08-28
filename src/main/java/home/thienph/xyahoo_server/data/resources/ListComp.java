package home.thienph.xyahoo_server.data.resources;

import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.users.ResourceContext;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListComp {
    String groupName;
    String key;
    String name;
    int status;
    String statusText;
    int iconId;
    byte[] iconData;

    public ListComp(RoomContext roomContext) {
        this.groupName = roomContext.getRoomGroup().getGroupName();
        this.key = roomContext.getRoom().getRoomKey();
        this.name = roomContext.getRoom().getRoomName();
        this.status = roomContext.getRoomStatus();
        this.statusText = roomContext.getRoomStatusText();
        this.iconId = roomContext.getRoom().getIconId();
        this.iconData = roomContext.getIcon();
    }

    public ListComp(UserContext userContext, ResourceContext icon) {
        this.groupName = ScreenConstant.ROOM_LIST_FRIEND_SCREEN_NAME;
        this.key = userContext.getUsername();
        this.name = userContext.getUsername();
        this.status = 0;
        this.statusText = userContext.getUser().getStatusText();
        this.iconId = icon.getResourceEntity().getResourceId();
        this.iconData = icon.getResourceData();
    }
}
