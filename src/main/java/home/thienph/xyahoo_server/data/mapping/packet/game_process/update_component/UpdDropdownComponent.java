package home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component;

import io.netty.buffer.ByteBuf;

public class UpdDropdownComponent extends AUpdateComponent {
    int selectedIndex;


    public UpdDropdownComponent(int componentId, int selectedIndex) {
        super(componentId, 7);
        this.selectedIndex = selectedIndex;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeShort(selectedIndex);
    }
}
