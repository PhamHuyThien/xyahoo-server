package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class NewDialogProcess implements IGameProcessPacketPipeline {
    public static final boolean IS_SWITCH_LAST_SCREEN_DEFAULT = true;
    String title;
    int dialogId;
    boolean isSwitchLastScreen;

    public static NewDialogProcess createDefault(String title, int dialogId) {
        return new NewDialogProcess(title, dialogId, IS_SWITCH_LAST_SCREEN_DEFAULT);
    }

    public NewDialogProcess(String title, int dialogId, boolean isSwitchLastScreen) {
        this.title = title;
        this.dialogId = dialogId;
        this.isSwitchLastScreen = isSwitchLastScreen;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(4);
        XByteBuf.writeString(payload, title);
        payload.writeInt(dialogId);
        payload.writeBoolean(isSwitchLastScreen);
    }
}
