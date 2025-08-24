package home.thienph.xyahoo_server.data.builder;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class PacketPipeline implements IPipeline<PacketPipeline, APacketPipeline> {
    List<APacketPipeline> packetPipeline = new ArrayList<>();

    public PacketPipeline addPipeline(APacketPipeline packet) {
        packetPipeline.add(packet);
        return this;
    }

    public PacketPipeline endPipeline() {
        packetPipeline.forEach(APacketPipeline::action);
        return this;
    }

    public void flushPipeline(ChannelHandlerContext ctx) {
        packetPipeline.forEach(packet -> ctx.writeAndFlush(packet.getPacket()));
    }
}
