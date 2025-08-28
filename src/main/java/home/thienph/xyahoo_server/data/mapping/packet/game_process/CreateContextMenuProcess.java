package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.data.resources.ContextMenu;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class CreateContextMenuProcess implements IGameProcessPacketPipeline {
    public static final int MENU_TYPE_LEFT = 0;
    public static final int MENU_TYPE_CENTER = 1;
    public static final int MENU_TYPE_RIGHT = 2;
    int menuType; // 0,1,2
    List<ContextMenu> contextMenus;

    public CreateContextMenuProcess(int menuType, List<ContextMenu> contextMenus) {
        this.menuType = menuType;
        this.contextMenus = contextMenus;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(48);
        payload.writeByte(menuType);
        payload.writeByte(contextMenus.size());
        for (ContextMenu contextMenu : contextMenus) {
            XByteBuf.writeString(payload, contextMenu.getLabel());
            XByteBuf.writeByteArray(payload, contextMenu.getAction().endPipeline().getPayloadPipeline().array());
        }
    }
}
