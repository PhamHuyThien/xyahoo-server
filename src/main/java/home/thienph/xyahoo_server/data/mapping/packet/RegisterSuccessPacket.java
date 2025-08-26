package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

public class RegisterSuccessPacket extends APacketPipeline {

    public RegisterSuccessPacket() {
        super(-2);
    }

    @Override
    public APacketPipeline build() {
        return this;
    }
}
