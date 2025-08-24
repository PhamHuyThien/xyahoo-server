package home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component;

import home.thienph.xyahoo_server.data.builder.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class PopupDialogComponent extends AComponent {
    String message;
    byte dialogType;
    GameProcessPacketPipeline clickAction;

    public PopupDialogComponent(String message, byte dialogType, GameProcessPacketPipeline clickAction) {
        super(16);
        this.message = message;
        this.dialogType = dialogType;
        this.clickAction = clickAction;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, message);
        payload.writeByte(dialogType);
        XByteBuf.writeByteArray(payload, clickAction.endPipeline().getPayloadPipeline().array());
    }
}
