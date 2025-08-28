package home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class UpdTextLinkComponent extends AUpdateComponent {
    String linkText;
    GameProcessPacketPipeline action;

    public UpdTextLinkComponent(int componentId, String linkText, GameProcessPacketPipeline action) {
        super(componentId, 4);
        this.linkText = linkText;
        this.action = action;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, linkText);
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
    }
}
