package home.thienph.xyahoo_server.data.requests;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFriendReq implements MappingRequestPayload<AddFriendReq> {
    String username;

    @Override
    public AddFriendReq mapping(ByteBuf payload) {
        username = XByteBuf.readString(payload);
        return this;
    }
}
