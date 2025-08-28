package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.resources.GridComp;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class GridCreateComponent extends ACreateComponent {
    public static final int COLUMN_DEFAULT = 40;
    public static final int ROW_DEFAULT = 40;
    public static final boolean HAS_CHECKBOX_DEFAULT = true;
    int column;
    int row;
    boolean hasCheckBox;
    List<GridComp> grids;
    GameProcessPacketPipeline clickAction;

    public static GridCreateComponent createDefault(List<GridComp> grids, GameProcessPacketPipeline clickAction) {
        return new GridCreateComponent(COLUMN_DEFAULT, ROW_DEFAULT, HAS_CHECKBOX_DEFAULT, grids, clickAction);
    }

    public GridCreateComponent(int column, int row, boolean hasCheckBox, List<GridComp> grids, GameProcessPacketPipeline clickAction) {
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

        for (GridComp grid : grids) {
            XByteBuf.writeString(payload, grid.getText());
            payload.writeInt(grid.getActionId());
            payload.writeInt(grid.getImageId());
            if (hasCheckBox)
                payload.writeBoolean(grid.isChecked());
        }
        XByteBuf.writeByteArray(payload, clickAction.endPipeline().getPayloadPipeline().array());
    }
}
