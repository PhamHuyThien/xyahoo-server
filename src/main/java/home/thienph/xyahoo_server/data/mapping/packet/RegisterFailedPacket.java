package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

public class RegisterFailedPacket extends APacketPipeline {

    public RegisterFailedPacket() {
        super(-3);
    }

    @Override
    public APacketPipeline build() {
        return this;
    }
}
