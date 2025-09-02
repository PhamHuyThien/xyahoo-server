package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class PhotoViewProcess implements IGameProcessPacketPipeline {
    int screenId;
    String title;
    String caption;
    int displayMode;
    byte[] imageData;
    int photoId;

    public PhotoViewProcess(int screenId, String title, String caption, int displayMode, byte[] imageData, int photoId) {
        this.screenId = screenId;
        this.title = title;
        this.caption = caption;
        this.displayMode = displayMode;
        this.imageData = imageData;
        this.photoId = photoId;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(54);
        payload.writeInt(screenId);
        XByteBuf.writeString(payload, title);
        XByteBuf.writeString(payload, caption);
        payload.writeInt(displayMode);
        XByteBuf.writeByteArray(payload, imageData);
        payload.writeInt(photoId);
    }
}
