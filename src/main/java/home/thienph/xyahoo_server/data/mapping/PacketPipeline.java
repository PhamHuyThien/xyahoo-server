package home.thienph.xyahoo_server.data.mapping;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class PacketPipeline implements IPipeline<APacketPipeline, PacketPipeline> {
    List<APacketPipeline> packetPipeline = new ArrayList<>();

    public static PacketPipeline newInstance() {
        return new PacketPipeline();
    }

    public PacketPipeline addPipeline(APacketPipeline data) {
        packetPipeline.add(data);
        return this;
    }

    public PacketPipeline addPipeline(IPipelineGroup<APacketPipeline> function) {
        packetPipeline.add(function.group());
        return this;
    }

    public PacketPipeline endPipeline() {
        packetPipeline.forEach(APacketPipeline::build);
        return this;
    }

    public void flushPipeline(Channel channel) {
        packetPipeline.forEach(packet -> channel.writeAndFlush(packet.getPacket()));
    }
}
