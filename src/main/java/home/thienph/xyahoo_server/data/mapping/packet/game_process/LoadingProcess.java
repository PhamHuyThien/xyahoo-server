package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class LoadingProcess implements IGameProcessPacketPipeline {
    boolean status;

    public LoadingProcess(boolean status) {
        this.status = status;
    }

    @Override
    public void build(ByteBuf payload) {
        if (status) payload.writeInt(52);
        else payload.writeInt(53);
    }
}
