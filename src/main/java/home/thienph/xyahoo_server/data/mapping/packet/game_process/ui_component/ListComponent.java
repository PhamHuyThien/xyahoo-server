package home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.friends.BuddyInfo;
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
    List<BuddyInfo> buddyInfoList;
    GameProcessPacketPipeline action;

    public ListComponent(boolean adjustHeight, byte enableDescription, byte iconType, int iconWidth, int iconHeight, byte enableStatusIcon, List<BuddyInfo> buddyInfoList, GameProcessPacketPipeline action) {
        super(11);
        this.adjustHeight = adjustHeight;
        this.enableDescription = enableDescription;
        this.iconType = iconType;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.enableStatusIcon = enableStatusIcon;
        this.buddyInfoList = buddyInfoList;
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
        payload.writeInt(buddyInfoList.size());
        for (BuddyInfo buddyInfo : buddyInfoList) {
            XByteBuf.writeString(payload, buddyInfo.getGroupName());
            XByteBuf.writeString(payload, buddyInfo.getMediaExtension());
            XByteBuf.writeString(payload, buddyInfo.getDisplayName());
            if (enableDescription == 1) {
                XByteBuf.writeString(payload, buddyInfo.getDescription());
            }
            if (iconType == 2) {
                payload.writeInt(buddyInfo.getImageSourceId());
            } else if (iconType == 3) {
                XByteBuf.writeByteArray(payload, buddyInfo.getMediaData());
            }
            if (enableStatusIcon == 1) {
                payload.writeByte(buddyInfo.getStatusCode());
            }
            XByteBuf.writeString(payload, buddyInfo.getStatusDescription());
        }
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
    }
}
