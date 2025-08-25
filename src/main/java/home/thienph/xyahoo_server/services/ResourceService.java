package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.builder.PacketPipeline;
import home.thienph.xyahoo_server.data.builder.packet.CacheImagePacket;
import home.thienph.xyahoo_server.data.requests.ReqReloadDataReq;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@AllArgsConstructor
public class ResourceService {

    @Autowired
    SimulatorService simulatorService;


    @SneakyThrows
    public void reqReloadData(ChannelHandlerContext ctx, ReqReloadDataReq req) {
        Packet packet = new Packet(123);
        packet.getPayload().writeInt(req.getSourceId());
        packet.getPayload().writeInt(1);
        XByteBuf.writeByteArray(packet.getPayload(), Files.readAllBytes(Path.of("data/images/Icn1.png")));
        ctx.writeAndFlush(packet);
    }

    @SneakyThrows
    public void loadResource(Channel ctx) {
        PacketPipeline.newInstance()
                .addPipeline(new CacheImagePacket(100, "data/images/Icn0.png"))
                .addPipeline(new CacheImagePacket(101, "data/images/Icn1.png"))
                .addPipeline(new CacheImagePacket(102, "data/images/Icn2.png"))
                .addPipeline(new CacheImagePacket(103, "data/images/Icn3.png"))
                .endPipeline()
                .flushPipeline(ctx);
    }
}
