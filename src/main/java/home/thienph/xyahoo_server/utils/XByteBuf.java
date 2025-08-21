package home.thienph.xyahoo_server.utils;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

@UtilityClass
public class XByteBuf {

    public String readString(ByteBuf buf) {
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
    public void writeString(ByteBuf buf, String value) {
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

    public byte[] readByteArray(ByteBuf buf) {
        int length = buf.readInt(); // đọc độ dài mảng byte
        byte[] data = new byte[length];
        buf.readBytes(data); // copy vào data
        return data;
    }

    public void writeByteArray(ByteBuf buf, byte[] data) {
        if (data == null) {
            buf.writeInt(0); // nếu null thì ghi length = 0
            return;
        }
        buf.writeInt(data.length);   // ghi độ dài
        buf.writeBytes(data);        // ghi nội dung
    }
}
