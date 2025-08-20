package home.thienph.xyahoo_server.data.requests;

import home.thienph.xyahoo_server.utils.ByteBufUtil;
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

    String userName;
    String password;
    int type;
    String string;

    @Override
    public LoginReq mapping(ByteBuf payload) {
        this.userName = ByteBufUtil.readString(payload);
        this.password = ByteBufUtil.readString(payload);
        this.type = payload.readInt();
        this.string = ByteBufUtil.readString(payload);
        return this;
    }
}
