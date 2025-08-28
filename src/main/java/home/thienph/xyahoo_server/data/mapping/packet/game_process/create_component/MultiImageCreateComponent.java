package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class MultiImageCreateComponent extends ACreateComponent {
    List<Integer> imageIds;
    int width;
    int height;
    boolean isVisible;
    boolean hasBorder;
    String clickActionName;
    GameProcessPacketPipeline clickAction;
    byte alignment;

    public MultiImageCreateComponent(List<Integer> imageIds, int width, int height, boolean isVisible, boolean hasBorder, String clickActionName, GameProcessPacketPipeline clickAction, byte alignment) {
        super(8);
        this.imageIds = imageIds;
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
        payload.writeByte(imageIds.size());
        for (Integer imageId : imageIds) {
            payload.writeInt(imageId);
        }
        payload.writeInt(width);
        payload.writeInt(height);
        payload.writeBoolean(isVisible);
        payload.writeBoolean(hasBorder);
        XByteBuf.writeString(payload, clickActionName);
        XByteBuf.writeByteArray(payload, clickAction.endPipeline().getPayloadPipeline().array());
        payload.writeByte(alignment);
    }
}
