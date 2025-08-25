package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class FocusComponentProcess implements IGameProcessPacketPipeline {
    int screenId;
    int componentId;

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(7);
        payload.writeInt(screenId);
        payload.writeInt(componentId);
    }
}
