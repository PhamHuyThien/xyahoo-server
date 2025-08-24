package home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component;

import home.thienph.xyahoo_server.data.builder.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class DropdownComponent extends AComponent {
    String label;
    int selectedIndex;
    List<String> options;
    GameProcessPacketPipeline action;
    int finalSelectedIndex;
    int alignment;

    public DropdownComponent(String label, int selectedIndex, List<String> options, GameProcessPacketPipeline action, int finalSelectedIndex, int alignment) {
        super(7);
        this.label = label;
        this.selectedIndex = selectedIndex;
        this.options = options;
        this.action = action;
        this.finalSelectedIndex = finalSelectedIndex;
        this.alignment = alignment;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, label);
        payload.writeInt(selectedIndex);
        payload.writeInt(options.size());
        for (String option : options) {
            XByteBuf.writeString(payload, option);
        }
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
        payload.writeInt(finalSelectedIndex);
        payload.writeByte(alignment);
    }
}
