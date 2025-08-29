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
public class RegisterReq {

    String username;
    String password;
    int partnerId;
    int appId;

    public RegisterReq(ByteBuf payload) {
        this.username = XBase64.decodeWithReverse(XByteBuf.readString(payload));
        this.password = XBase64.decodeWithReverse(XByteBuf.readString(payload));
        this.partnerId = payload.readInt();
        this.appId = payload.readInt();
    }
}
