package home.thienph.xyahoo_server.data.requests;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFriendReq {
    String username;

    public AddFriendReq(ByteBuf payload) {
        username = XByteBuf.readString(payload);
    }
}
