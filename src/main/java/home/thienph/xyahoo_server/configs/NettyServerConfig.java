package home.thienph.xyahoo_server.configs;

import home.thienph.xyahoo_server.components.PacketDecoder;
import home.thienph.xyahoo_server.components.PacketEncoder;
import home.thienph.xyahoo_server.components.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public abstract class NettyServerConfig {

    @Value("${server.netty.port}")
    private int serverNettyPort;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    @Lookup
    public abstract ServerHandler serverHandler();

    @PostConstruct
    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
                ch.pipeline().addLast(new PacketDecoder());
                ch.pipeline().addLast(new PacketEncoder());
                ch.pipeline().addLast(serverHandler());
            }
        });
        ChannelFuture future = bootstrap.bind(serverNettyPort).sync();
        serverChannel = future.channel();
        log.info("Netty TCP Server started on port {} 🚀", serverNettyPort);
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Netty Server stopped port {} 🛑", serverNettyPort);
    }
}