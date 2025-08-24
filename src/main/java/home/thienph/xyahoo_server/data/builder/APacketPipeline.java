package home.thienph.xyahoo_server.data.builder;

import home.thienph.xyahoo_server.data.base.Packet;
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

    protected abstract void action();
}
