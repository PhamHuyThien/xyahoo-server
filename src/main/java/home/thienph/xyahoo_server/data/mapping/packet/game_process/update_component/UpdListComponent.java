package home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component;

import home.thienph.xyahoo_server.data.resources.ListComp;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class UpdListComponent extends AUpdateComponent {
    List<ListComp> listComponents;


    public UpdListComponent(int componentId, List<ListComp> listComponents) {
        super(componentId, 11);
        this.listComponents = listComponents;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeByte(listComponents.size());
        for (ListComp listComp : listComponents) {
            XByteBuf.writeString(payload, listComp.getGroupName());
            payload.writeInt(listComp.getIconId());
            XByteBuf.writeByteArray(payload, listComp.getIconData());
            XByteBuf.writeString(payload, listComp.getName());
        }
    }
}
