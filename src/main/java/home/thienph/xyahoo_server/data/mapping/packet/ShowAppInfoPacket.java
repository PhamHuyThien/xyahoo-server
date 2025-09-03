package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class ShowAppInfoPacket extends APacketPipeline {

    String appInfo;

    public ShowAppInfoPacket(String appInfo) {
        super(11712001);
        this.appInfo = appInfo;
    }

    @Override
    public APacketPipeline build() {
        XByteBuf.writeString(packet.getPayload(), appInfo);
        return this;
    }
}
