package home.thienph.xyahoo_server.data.mapping;

import home.thienph.xyahoo_server.data.users.UserContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.PromiseCombiner;

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

    public ChannelFuture flushPipeline(UserContext userContext) {
        Channel channel = userContext.getChannel();
        ChannelPromise aggregatePromise = channel.newPromise();
        PromiseCombiner combiner = new PromiseCombiner(channel.eventLoop());
        for (APacketPipeline packet : packetPipeline) {
            ChannelFuture future = channel.writeAndFlush(packet.getPacket());
            combiner.add(future);
        }
        combiner.finish(aggregatePromise);
        return aggregatePromise;
    }

}
