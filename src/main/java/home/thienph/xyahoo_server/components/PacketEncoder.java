package home.thienph.xyahoo_server.components;

import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.utils.XPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        log.debug("[{}] [{}] [OUT] commandId= {} typeId= {} payloadSize= {}",
                ctx.channel().id(),
                XPacket.getUsernameByChannel(ctx.channel()),
                msg.getCommandId(),
                msg.getTypeId(),
                msg.getPayload().readableBytes());
        int length = 4 + 4 + msg.getPayload().readableBytes(); // command + type + payload
        out.writeInt(length);
        out.writeInt(msg.getCommandId());
        out.writeInt(msg.getTypeId());
        out.writeBytes(msg.getPayload().duplicate());
    }
}