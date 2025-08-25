package home.thienph.xyahoo_server.data.mapping;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class PacketPipeline {
    List<APacketPipeline> packetPipeline = new ArrayList<>();

    public static PacketPipeline newInstance() {
        return new PacketPipeline();
    }

    public PacketPipeline addPipeline(APacketPipeline packet) {
        packetPipeline.add(packet);
        return this;
    }

    public PacketPipeline addPipeline(IPipelineGroup<APacketPipeline> pipelineGroup) {
        packetPipeline.add(pipelineGroup.groupPipeline());
        return this;
    }

    public PacketPipeline endPipeline() {
        packetPipeline.forEach(APacketPipeline::build);
        return this;
    }

    public void flushPipeline(Channel ctx) {
        packetPipeline.forEach(packet -> ctx.writeAndFlush(packet.getPacket()));
    }
}
