package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.base.Packet;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public class SimulatorService {
    void outputLoginSuccess(ChannelHandlerContext ctx) {
        Packet packet = new Packet(-5, 1);
        ctx.writeAndFlush(packet);
    }
}
