package home.thienph.xyahoo_server.managers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ConnectionManager {
    // Map<clientId, Channel>
    private static final Map<String, Channel> connections = new ConcurrentHashMap<>();

    public static void addConnection(String clientId, ChannelHandlerContext ctx) {
        connections.put(clientId, ctx.channel());
    }

    public static void removeConnection(String clientId) {
        connections.remove(clientId);
    }

    public static Channel getChannel(String clientId) {
        return connections.get(clientId);
    }

    public static void sendToClient(String clientId, byte[] data) {
        Channel ch = connections.get(clientId);
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(ch.alloc().buffer().writeBytes(data));
        }
    }
}
