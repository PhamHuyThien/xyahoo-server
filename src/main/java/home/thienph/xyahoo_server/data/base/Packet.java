package home.thienph.xyahoo_server.data.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Packet {
    int commandId;
    int typeId;
    ByteBuf payload = Unpooled.buffer(0);

    public Packet(int commandId, int typeId) {
        this.commandId = commandId;
        this.typeId = typeId;
    }
}
