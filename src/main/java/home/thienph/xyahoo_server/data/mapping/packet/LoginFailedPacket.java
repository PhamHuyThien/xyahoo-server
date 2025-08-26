package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

public class LoginFailedPacket extends APacketPipeline {

    public LoginFailedPacket() {
        super(-1);
    }

    @Override
    public APacketPipeline build() {
        return this;
    }
}
