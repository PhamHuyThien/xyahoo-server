package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class CreateSimpleDialogProcess implements IGameProcessPacketPipeline {
    String title;
    String leftButtonText;
    GameProcessPacketPipeline leftButtonAction;
    String middleButtonText;
    GameProcessPacketPipeline middleButtonAction;
    String rightButtonText;
    GameProcessPacketPipeline rightButtonAction;

    public CreateSimpleDialogProcess(String title, String leftButtonText, GameProcessPacketPipeline leftButtonAction, String middleButtonText, GameProcessPacketPipeline middleButtonAction, String rightButtonText, GameProcessPacketPipeline rightButtonAction) {
        this.title = title;
        this.leftButtonText = leftButtonText;
        this.leftButtonAction = leftButtonAction;
        this.middleButtonText = middleButtonText;
        this.middleButtonAction = middleButtonAction;
        this.rightButtonText = rightButtonText;
        this.rightButtonAction = rightButtonAction;
    }

    public CreateSimpleDialogProcess(String title) {
        this.title = title;
        this.leftButtonText = "";
        this.leftButtonAction = GameProcessPacketPipeline.newInstance();
        this.middleButtonText = "OK";
        this.middleButtonAction = GameProcessPacketPipeline.newInstance();
        this.rightButtonText = "";
        this.rightButtonAction = GameProcessPacketPipeline.newInstance();
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(29);
        XByteBuf.writeString(payload, title);
        XByteBuf.writeString(payload, leftButtonText);
        XByteBuf.writeByteArray(payload, leftButtonAction.endPipeline().getPayloadPipeline().array());
        XByteBuf.writeString(payload, middleButtonText);
        XByteBuf.writeByteArray(payload, middleButtonAction.endPipeline().getPayloadPipeline().array());
        XByteBuf.writeString(payload, rightButtonText);
        XByteBuf.writeByteArray(payload, rightButtonAction.endPipeline().getPayloadPipeline().array());
    }
}
