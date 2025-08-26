package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.HasRole;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.mapping.packet.ReceiveChatRoomMessagePacket;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    GameManager gameManager;

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.SEND_MESSAGE)
    public void chat(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        String roomId = XByteBuf.readString(packet.getPayload());
        String message = XByteBuf.readString(packet.getPayload());
        log.info("user {}: roomId: {}, message: {}", userContext.getUsername(), roomId, message);

        new Thread(() -> {
            while(true){
                ReceiveChatRoomMessagePacket receiveChatRoomMessagePacket = new ReceiveChatRoomMessagePacket(userContext.getUsername(), message, 1);
                channel.writeAndFlush(receiveChatRoomMessagePacket.build().getPacket());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.LEAVE_CHAT_ROOM)
    public void leaveChatRoom(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        String roomId = XByteBuf.readString(packet.getPayload());
        log.info("user {}: leave room roomId: {}", userContext.getUsername(), roomId);
    }
}
