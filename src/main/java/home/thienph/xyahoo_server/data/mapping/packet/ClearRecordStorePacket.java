package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

public class ClearRecordStorePacket extends APacketPipeline {
    public ClearRecordStorePacket() {
        super(5000040);
    }

    @Override
    public APacketPipeline build() {
        return this;
    }
}
