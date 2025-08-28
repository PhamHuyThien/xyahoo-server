package home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class UpdTextInputComponent extends AUpdateComponent {
    String inputText;
    String displayText;

    public UpdTextInputComponent(int componentId, String inputText, String displayText) {
        super(componentId, 0);
        this.inputText = inputText;
        this.displayText = displayText;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, inputText);
        XByteBuf.writeString(payload, displayText);
    }
}
