package home.thienph.xyahoo_server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Scope("prototype")
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Autowired
    private CommandHandler commandHandler;

    private final ByteBuf accumulation = Unpooled.buffer(); // bộ đệm tích luỹ

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        try {
            accumulation.writeBytes(msg); // tích luỹ dữ liệu nhận được

            while (accumulation.readableBytes() >= 12) { // headerLength (4) + commandId (4) + typeId (4)
                accumulation.markReaderIndex();

                int payloadLength = accumulation.readInt();
                int commandId = accumulation.readInt();
                int typeId = accumulation.readInt();

                if (accumulation.readableBytes() < payloadLength - 8) {
                    // chưa đủ dữ liệu, reset lại
                    accumulation.resetReaderIndex();
                    break;
                }

                ByteBuf payloadBuf = accumulation.readSlice(payloadLength - 8).retain();

                commandHandler.executeCommand(ctx, commandId, typeId, payloadBuf);

                log.info("payloadLength: {}", payloadLength);
                log.info("commandId: {}", commandId);
                log.info("typeId: {}", typeId);
                log.info("-----------------------------------");
            }
        } catch (Exception e) {
            log.error("❌ Error reading payload: {}", e.getMessage());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asShortText();
        ConnectionManager.addConnection(channelId, ctx);
        System.out.println("Client connected: " + channelId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asShortText();
        ConnectionManager.removeConnection(channelId);
        System.out.println("Client disconnected: " + channelId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("❌ Client disconnected: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}