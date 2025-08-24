package home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AComponent {
    int componentType;
    public abstract void build(ByteBuf payload);
}
