package home.thienph.xyahoo_server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@AllArgsConstructor
public class CommandHandler {

    public void executeCommand(ChannelHandlerContext ctx, int commandId, int typeId, ByteBuf payload) {
        switch (commandId) {
            case 5000007:
                log.info("userName: {}", readString(payload));
                log.info("password: {}", readString(payload));
                log.info("int: {}", payload.readInt());
                log.info("string: {}", readString(payload));

                int command = -5;
                int type = 1;
                byte[] payloadSend = new byte[0];
                int length = Integer.BYTES + Integer.BYTES + payloadSend.length;

                ByteBuf response = Unpooled.buffer();
                response.writeInt(length);
                response.writeInt(command);
                response.writeInt(type);
                response.writeBytes(payloadSend);
                ctx.writeAndFlush(response);
                break;
            case 5018:

//                command = 1001; // moi vao nhom
//                command = 1005; // phong chat
//                command = 1009; // show danh sach nguoi ket ban thi phai
//                command = 1020; //
                command = 49; //
                type = 1;

                ByteBuf buf = Unpooled.buffer();
//                buf.writeInt(1);
                writeString(buf, "thiendeptrai");
                buf.writeInt(1);

                length = Integer.BYTES + Integer.BYTES + buf.readableBytes();

                response = Unpooled.buffer();
                response.writeInt(length);
                response.writeInt(command);
                response.writeInt(type);
                response.writeBytes(buf);
                ctx.writeAndFlush(response);

                break;
        }
    }

    public static String readString(ByteBuf buf) {
        // Bước 1: đọc số ký tự
        int length = buf.readInt();

        // Bước 2: đọc từng char (2 byte big-endian)
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            byte high = buf.readByte();
            byte low = buf.readByte();
            chars[i] = (char) (((high & 0xFF) << 8) | (low & 0xFF));
        }

        // Bước 3: trả về String
        return new String(chars);
    }

    // Utility cho String
    public static void writeString(ByteBuf buf, String value) {
        if (value == null) {
            buf.writeInt(0);
            return;
        }

        int length = value.length();
        buf.writeInt(length); // ghi độ dài string

        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            buf.writeByte((byte) (c >> 8));  // ghi byte high
            buf.writeByte((byte) c);         // ghi byte low
        }
    }
}
