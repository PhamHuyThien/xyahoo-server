package home.thienph.xyahoo_server.data.requests;

import io.netty.buffer.ByteBuf;

public interface MappingRequestPayload<T> {
    T mapping(ByteBuf payload);
}
