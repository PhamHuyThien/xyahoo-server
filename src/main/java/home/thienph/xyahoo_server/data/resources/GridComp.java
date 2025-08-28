package home.thienph.xyahoo_server.data.resources;

import home.thienph.xyahoo_server.entities.GameHomeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GridComp {
    String text;
    int actionId;
    int imageId;
    boolean checked;

    public GridComp(GameHomeEntity gameHomeEntity){
        text = gameHomeEntity.getTitle();
        actionId = gameHomeEntity.getScreenId();
        imageId = gameHomeEntity.getImageId();
        checked = gameHomeEntity.getChecked() == 1;
    }
}
