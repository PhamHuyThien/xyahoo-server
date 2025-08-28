package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class PopupDialogCreateComponent extends ACreateComponent {
    public static final int DIALOG_TYPE_OK = 0;
    String message;
    int dialogType;
    GameProcessPacketPipeline clickAction;

    public PopupDialogCreateComponent(String message, int dialogType, GameProcessPacketPipeline clickAction) {
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
