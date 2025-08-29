package home.thienph.xyahoo_server.data.requests;

import home.thienph.xyahoo_server.utils.XBase64;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginReq {

    String username;
    String password;
    int type;
    String string;

    public LoginReq(ByteBuf payload) {
        this.username = XBase64.decodeWithReverse(XByteBuf.readString(payload));
        this.password = XBase64.decodeWithReverse(XByteBuf.readString(payload));
        this.type = payload.readInt();
        this.string = XByteBuf.readString(payload);
    }
}
