package home.thienph.xyahoo_server.components;

import home.thienph.xyahoo_server.configs.PacketHandlerRegistry;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Scope("prototype")
public class ServerHandler extends SimpleChannelInboundHandler<Packet> {

    @Autowired
    PacketHandlerRegistry packetHandlerRegistry;

    @Autowired
    GameManager gameManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        String sessionId = ctx.channel().id().asShortText();
        UserContext userContext = gameManager.getUserContexts().get(sessionId);
        packetHandlerRegistry.handle(userContext, packet);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String sessionId = channel.id().asShortText();
        gameManager.getUserContexts().put(sessionId, new UserContext(channel));
        log.debug("Client connected: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        gameManager.destroySessionUserByChannelId(ctx.channel().id().asShortText());
        log.debug("Client disconnected: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        gameManager.destroySessionUserByChannelId(ctx.channel().id().asShortText());
        log.debug("Client disconnected: {}", ctx.channel().remoteAddress(), cause);
    }
}