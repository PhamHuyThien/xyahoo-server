package home.thienph.xyahoo_server.data.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Grid {
    String text;
    int actionId;
    int imageId;
    boolean checked;
}
