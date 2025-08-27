package home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.entities.GameHomeEntity;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class GridComponent extends AComponent {
    int column;
    int row;
    boolean hasCheckBox;
    List<GameHomeEntity> grids;
    GameProcessPacketPipeline clickAction;

    public GridComponent(int column, int row, boolean hasCheckBox, List<GameHomeEntity> grids, GameProcessPacketPipeline clickAction) {
        super(13);
        this.column = column;
        this.row = row;
        this.hasCheckBox = hasCheckBox;
        this.grids = grids;
        this.clickAction = clickAction;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(grids.size());
        payload.writeInt(column);
        payload.writeInt(row);
        payload.writeBoolean(hasCheckBox);

        for (GameHomeEntity grid : grids) {
            XByteBuf.writeString(payload, grid.getTitle());
            payload.writeInt(grid.getScreenId());
            payload.writeInt(grid.getImageId());
            if (hasCheckBox)
                payload.writeBoolean(grid.getChecked() == 1);
        }
        XByteBuf.writeByteArray(payload, clickAction.endPipeline().getPayloadPipeline().array());
    }
}
