package home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.friends.BuddyInfo;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class ListComponent extends AComponent {
    boolean adjustHeight;
    byte enableDescription; //1 enable , 0 disable
    byte iconType; // 2: localIcon, 3: remoteIcon
    int iconWidth;
    int iconHeight;
    byte enableStatusIcon; // 1 enable, 0 disable
    List<RoomContext> roomContexts;
    GameProcessPacketPipeline action;

    public ListComponent(boolean adjustHeight, byte enableDescription, byte iconType, int iconWidth, int iconHeight, byte enableStatusIcon, List<RoomContext> roomContexts, GameProcessPacketPipeline action) {
        super(11);
        this.adjustHeight = adjustHeight;
        this.enableDescription = enableDescription;
        this.iconType = iconType;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.enableStatusIcon = enableStatusIcon;
        this.roomContexts = roomContexts;
        this.action = action;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeBoolean(adjustHeight);
        payload.writeByte(enableDescription);
        payload.writeByte(iconType);
        if (iconType == 2) {
            payload.writeInt(iconWidth);
            payload.writeInt(iconHeight);
        }
        payload.writeByte(enableStatusIcon);
        payload.writeInt(roomContexts.size());
        for (RoomContext roomContext : roomContexts) {
            XByteBuf.writeString(payload, roomContext.getRoomGroup().getGroupName());
            XByteBuf.writeString(payload, roomContext.getRoom().getRoomKey());
            XByteBuf.writeString(payload, roomContext.getRoom().getRoomName());
            if (enableDescription == 1) {
                XByteBuf.writeString(payload, roomContext.getRoomStatusText());
            }
            if (iconType == 2) {
                payload.writeInt(roomContext.getRoom().getIconId());
            } else if (iconType == 3) {
                XByteBuf.writeByteArray(payload, roomContext.getIcon());
            }
            if (enableStatusIcon == 1) {
                payload.writeByte(roomContext.getRoomStatus());
            }
            XByteBuf.writeString(payload, roomContext.getRoomStatusText());
        }
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
    }
}
