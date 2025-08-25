package home.thienph.xyahoo_server.data.requests;

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
public class LoginReq implements MappingRequestPayload<LoginReq> {

    String username;
    String password;
    int type;
    String string;

    @Override
    public LoginReq mapping(ByteBuf payload) {
        this.username = XByteBuf.readString(payload);
        this.password = XByteBuf.readString(payload);
        this.type = payload.readInt();
        this.string = XByteBuf.readString(payload);
        return this;
    }
}
