package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class ImageSourceCreateComponent extends ACreateComponent {
    int imageResourceId;
    int width;
    int height;
    boolean isVisible;
    boolean hasBorder;
    String clickActionName;
    GameProcessPacketPipeline clickAction;
    byte alignment;

    public ImageSourceCreateComponent(int imageResourceId, int width, int height, boolean isVisible, boolean hasBorder, String clickActionName, GameProcessPacketPipeline clickAction, byte alignment) {
        super(6);
        this.imageResourceId = imageResourceId;
        this.width = width;
        this.height = height;
        this.isVisible = isVisible;
        this.hasBorder = hasBorder;
        this.clickActionName = clickActionName;
        this.clickAction = clickAction;
        this.alignment = alignment;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(imageResourceId);
        payload.writeInt(width);
        payload.writeInt(height);
        payload.writeBoolean(isVisible);
        payload.writeBoolean(hasBorder);
        XByteBuf.writeString(payload, clickActionName);
        XByteBuf.writeByteArray(payload, clickAction.endPipeline().getPayloadPipeline().array());
        payload.writeByte(alignment);
    }
}
