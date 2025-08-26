package home.thienph.xyahoo_server.data.requests;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class RejectApproveFriendReq {
    Long userId;
    boolean approve;

    public RejectApproveFriendReq(ByteBuf payload) {
        userId = payload.readLong();
        approve = payload.readBoolean();
    }
}
