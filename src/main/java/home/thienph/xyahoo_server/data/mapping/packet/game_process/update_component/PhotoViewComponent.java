package home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class PhotoViewComponent extends AUpdateComponent {
    String title;
    String caption;
    int displayMode;
    byte[] imageData;


    public PhotoViewComponent(int componentId, String title, String caption, int displayMode, byte[] imageData) {
        super(componentId, 18);
        this.title = title;
        this.caption = caption;
        this.displayMode = displayMode;
        this.imageData = imageData;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, title);
        XByteBuf.writeString(payload, caption);
        payload.writeInt(displayMode);
        XByteBuf.writeByteArray(payload, imageData);
    }
}
