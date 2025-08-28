package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.resources.ListComp;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class ListCreateComponent extends ACreateComponent {
    public static final boolean ADJUST_HEIGHT_ENABLE = true;
    public static final boolean ADJUST_HEIGHT_DISABLE = false;
    public static final int DESCRIPTION_ENABLE = 1;
    public static final int DESCRIPTION_DISABLE = 0;
    public static final int ICON_LOCAL = 2;
    public static final int ICON_REMOTE = 3;
    public static final int ICON_DEFAULT_WIDTH = 10;
    public static final int ICON_DEFAULT_HEIGHT = 10;
    public static final int STATUS_ICON_ENABLE = 1;
    public static final int STATUS_ICON_DISABLE = 0;

    boolean adjustHeight;
    int enableDescription; //1 enable , 0 disable
    int iconType; // 2: localIcon, 3: remoteIcon
    int iconWidth;
    int iconHeight;
    int enableStatusIcon; // 1 enable, 0 disable
    List<ListComp> listComponents;
    GameProcessPacketPipeline action;

    public static ListCreateComponent createListDefault(List<ListComp> listComponents, GameProcessPacketPipeline action) {
        return new ListCreateComponent(ListCreateComponent.ADJUST_HEIGHT_ENABLE,
                ListCreateComponent.DESCRIPTION_DISABLE,
                ListCreateComponent.ICON_REMOTE,
                ListCreateComponent.ICON_DEFAULT_WIDTH,
                ListCreateComponent.ICON_DEFAULT_HEIGHT,
                ListCreateComponent.STATUS_ICON_ENABLE,
                listComponents,
                action);
    }

    public ListCreateComponent(boolean adjustHeight, int enableDescription, int iconType, int iconWidth, int iconHeight, int enableStatusIcon, List<ListComp> listComponents, GameProcessPacketPipeline action) {
        super(11);
        this.adjustHeight = adjustHeight;
        this.enableDescription = enableDescription;
        this.iconType = iconType;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.enableStatusIcon = enableStatusIcon;
        this.listComponents = listComponents;
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
        payload.writeInt(listComponents.size());
        for (ListComp listComp : listComponents) {
            XByteBuf.writeString(payload, listComp.getGroupName());
            XByteBuf.writeString(payload, listComp.getKey());
            XByteBuf.writeString(payload, listComp.getName());
            if (enableDescription == 1) {
                XByteBuf.writeString(payload, listComp.getStatusText());
            }
            if (iconType == 2) {
                payload.writeInt(listComp.getIconId());
            } else if (iconType == 3) {
                XByteBuf.writeByteArray(payload, listComp.getIconData());
            }
            if (enableStatusIcon == 1) {
                payload.writeByte(listComp.getStatus());
            }
            XByteBuf.writeString(payload, listComp.getStatusText());
        }
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
    }
}
