package home.thienph.xyahoo_server.data.mapping;

import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.users.UserContext;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class APacketPipeline {
    protected Packet packet;

    public APacketPipeline(int commandId) {
        this.packet = new Packet(commandId);
    }

    public APacketPipeline(int commandId, int typeId) {
        this.packet = new Packet(commandId, typeId);
    }

    public APacketPipeline(Packet packet) {
        this.packet = packet;
    }

    public abstract APacketPipeline build();

    public void flush(UserContext userContext) {
        userContext.getChannel().writeAndFlush(packet);
    }
}
