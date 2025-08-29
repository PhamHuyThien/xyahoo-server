package home.thienph.xyahoo_server.data.requests;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class ReqReloadDataReq {
    int sourceId;

    public ReqReloadDataReq(ByteBuf payload) {
        sourceId = payload.readInt();
    }
}
