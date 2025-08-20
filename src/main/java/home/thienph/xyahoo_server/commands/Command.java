package home.thienph.xyahoo_server.commands;

import home.thienph.xyahoo_server.data.base.Packet;
import io.netty.channel.ChannelHandlerContext;

public interface Command {
    void execute(ChannelHandlerContext ctx, Packet packet);
}
