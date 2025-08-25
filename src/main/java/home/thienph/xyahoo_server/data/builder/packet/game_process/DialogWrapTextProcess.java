package home.thienph.xyahoo_server.data.builder.packet.game_process;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogWrapTextProcess implements IGameProcessPacketPipeline {
    String text;

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(28);
        XByteBuf.writeString(payload, text);
    }
}
