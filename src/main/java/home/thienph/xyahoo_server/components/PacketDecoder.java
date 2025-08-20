package home.thienph.xyahoo_server.components;

import home.thienph.xyahoo_server.data.base.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int command = in.readInt();
        int type = in.readInt();
        ByteBuf payload = in.readBytes(in.readableBytes()); // copy payload
        Packet packet = new Packet(command, type, payload);
        log.debug("[{}] [IN] {}", ctx.channel().id(), packet);
        out.add(packet);
    }
}
