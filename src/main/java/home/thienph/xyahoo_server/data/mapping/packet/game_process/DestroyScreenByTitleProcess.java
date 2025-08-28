package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class DestroyScreenByTitleProcess implements IGameProcessPacketPipeline {
    String title;

    public DestroyScreenByTitleProcess(String title) {
        this.title = title;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(100);
        XByteBuf.writeString(payload, title);
    }
}
