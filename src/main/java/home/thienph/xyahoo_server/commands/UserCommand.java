package home.thienph.xyahoo_server.commands;

import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.requests.LoginReq;
import home.thienph.xyahoo_server.services.AuthService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserCommand")
@Slf4j
public class UserCommand implements Command {


    @Autowired
    AuthService authService;

    @Override
    public void execute(ChannelHandlerContext ctx, Packet packet) {
        switch (packet.getCommandId()) {
            case ClientCommandConst.LOGIN:

                LoginReq loginReq = new LoginReq().mapping(packet.getPayload());
                authService.login(ctx, loginReq);



                break;
            case 5018:

//                command = 1001; // moi vao nhom
//                command = 1005; // phong chat
//                command = 1009; // show danh sach nguoi ket ban thi phai
//                command = 1020; //
//                command = 49; //
//                command = 4; //
//                type = 1;
//
//                ByteBuf buf = Unpooled.buffer();
////                buf.writeInt(1);
//                writeString(buf, "thiendeptrai");
//                buf.writeInt(-1);
//                buf.writeBoolean(true);
//
//                length = Integer.BYTES + Integer.BYTES + buf.readableBytes();
//
//                response = Unpooled.buffer();
//                response.writeInt(length);
//                response.writeInt(command);
//                response.writeInt(type);
//                response.writeBytes(buf);
//                ctx.writeAndFlush(response);
//
//                break;
        }
    }
}
