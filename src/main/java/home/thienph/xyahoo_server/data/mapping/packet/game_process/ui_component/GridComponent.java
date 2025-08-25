package home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.resources.Grid;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class GridComponent extends AComponent {
    int column;
    int row;
    boolean hasCheckBox;
    List<Grid> grids;
    GameProcessPacketPipeline clickAction;

    public GridComponent(int column, int row, boolean hasCheckBox, List<Grid> grids, GameProcessPacketPipeline clickAction) {
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

        for (Grid grid : grids) {
            XByteBuf.writeString(payload, grid.getText());
            payload.writeInt(grid.getActionId());
            payload.writeInt(grid.getImageId());
            if(hasCheckBox)
                payload.writeBoolean(grid.isChecked());
        }

        XByteBuf.writeByteArray(payload, clickAction.endPipeline().getPayloadPipeline().array());
    }
}
