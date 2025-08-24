package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.requests.ReqReloadDataReq;
import home.thienph.xyahoo_server.utils.XByteBuf;
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
}
