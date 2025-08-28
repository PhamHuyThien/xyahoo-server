package home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component;

import io.netty.buffer.ByteBuf;

public class UpdCheckboxComponent extends AUpdateComponent {

    boolean isChecked;

    public UpdCheckboxComponent(int componentId, boolean isChecked) {
        super(componentId, 14);
        this.isChecked = isChecked;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeBoolean(isChecked);
    }
}
