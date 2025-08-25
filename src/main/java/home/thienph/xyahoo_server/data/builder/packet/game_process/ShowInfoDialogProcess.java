package home.thienph.xyahoo_server.data.builder.packet.game_process;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class ShowInfoDialogProcess implements IGameProcessPacketPipeline {
    boolean loading;
    String title;

    public ShowInfoDialogProcess(boolean loading, String title) {
        this.loading = loading;
        this.title = title;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(1);
        payload.writeBoolean(loading);
        XByteBuf.writeString(payload, title);
    }
}
