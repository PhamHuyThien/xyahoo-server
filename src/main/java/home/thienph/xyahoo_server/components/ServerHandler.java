package home.thienph.xyahoo_server.components;

import home.thienph.xyahoo_server.commands.Command;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.managers.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Scope("prototype")
public class ServerHandler extends SimpleChannelInboundHandler<Packet> {

    @Autowired
    @Qualifier("UserCommand")
    private Command command;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        command.execute(ctx, packet);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asShortText();
        ConnectionManager.addConnection(channelId, ctx);
        log.debug("Client connected: {}", channelId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asShortText();
        ConnectionManager.removeConnection(channelId);
        log.debug("Client disconnected: {}", channelId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Client disconnected: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}