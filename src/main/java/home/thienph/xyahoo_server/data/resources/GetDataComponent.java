package home.thienph.xyahoo_server.data.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetDataComponent {
    byte dataType; //0 (INTEGER), 1 (STRING), 2 (BOOLEAN), 3 (ARRAY)
    int sourceType; // 0: use default value, 1: use screen id value
    int screenId;
    int componentId;
    boolean isRequired;
    Object defaultValue;
}
