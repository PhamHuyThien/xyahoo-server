package home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import lombok.Builder;

@Builder
public class TextInputComponent extends AComponent {
    String label;
    int maxLength;
    int inputType;
    byte alignment;

    public TextInputComponent(String label, int maxLength, int inputType, byte alignment) {
        super(0);
        this.label = label;
        this.maxLength = maxLength;
        this.inputType = inputType;
        this.alignment = alignment;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, label);
        payload.writeInt(maxLength);
        payload.writeInt(inputType);
        payload.writeByte(alignment);
    }
}
