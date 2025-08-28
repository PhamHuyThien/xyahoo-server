package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class TableDialogButtonProcess implements IGameProcessPacketPipeline {
    String title;
    int type;
    List<List<String>> data;
    String leftActionName;
    byte[] leftAction;
    String middleActionName;
    byte[] middleAction;
    String rightActionName;
    byte[] rightAction;

    public TableDialogButtonProcess(String title, int type, List<List<String>> data, String leftActionName, byte[] leftAction, String middleActionName, byte[] middleAction, String rightActionName, byte[] rightAction) {
        this.title = title;
        this.type = type;
        this.data = data;
        this.leftActionName = leftActionName;
        this.leftAction = leftAction;
        this.middleActionName = middleActionName;
        this.middleAction = middleAction;
        this.rightActionName = rightActionName;
        this.rightAction = rightAction;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(61);
        XByteBuf.writeString(payload, title);
        payload.writeByte(data.size());
        payload.writeByte(data.get(0).size());
        payload.writeByte(type);
        for(List<String> row : data) {
            for(String col : row) {
                XByteBuf.writeString(payload, col);
            }
        }
        XByteBuf.writeString(payload, leftActionName);
        XByteBuf.writeByteArray(payload, leftAction);
        XByteBuf.writeString(payload, middleActionName);
        XByteBuf.writeByteArray(payload, middleAction);
        XByteBuf.writeString(payload, rightActionName);
        XByteBuf.writeByteArray(payload, rightAction);
    }
}
