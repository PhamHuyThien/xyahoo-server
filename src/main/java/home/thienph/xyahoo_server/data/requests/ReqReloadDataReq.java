package home.thienph.xyahoo_server.data.requests;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class ReqReloadDataReq implements MappingRequestPayload<ReqReloadDataReq> {
    int sourceId;

    @Override
    public ReqReloadDataReq mapping(ByteBuf payload) {
        sourceId = payload.readInt();
        return this;
    }
}
