package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class ACreateComponent {
    int componentType;
    public abstract void build(ByteBuf payload);
}
