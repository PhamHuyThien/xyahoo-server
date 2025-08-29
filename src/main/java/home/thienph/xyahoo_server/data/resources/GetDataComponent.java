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
    public static final int DATA_TYPE_INTEGER = 0;
    public static final int DATA_TYPE_STRING = 1;
    public static final int DATA_TYPE_BOOLEAN = 2;
    public static final int DATA_TYPE_STRING_ARRAY = 3;
    public static final int SOURCE_TYPE_GET_SERVER_VALUE = 0;
    public static final int SOURCE_TYPE_GET_CLIENT_VALUE = 1;
    public static final boolean IS_REQUIRED_TRUE = true;
    public static final boolean IS_REQUIRED_FALSE = false;
    public static final Object DEFAULT_VALUE_NULL = null;
    int dataType; //0 (INTEGER), 1 (STRING), 2 (BOOLEAN), 3 (ARRAY)
    int sourceType; // 0: use default value, 1: use screen userId value
    int screenId;
    int componentId;
    boolean isRequired;
    Object defaultValue;

    public static GetDataComponent createGetDataStringDefault(int screenId, int componentId) {
        return new GetDataComponent(
                GetDataComponent.DATA_TYPE_STRING,
                GetDataComponent.SOURCE_TYPE_GET_CLIENT_VALUE,
                screenId,
                componentId,
                GetDataComponent.IS_REQUIRED_FALSE,
                GetDataComponent.DEFAULT_VALUE_NULL);
    }
    public static GetDataComponent createGetDataIntegerDefault(int screenId, int componentId) {
        return new GetDataComponent(
                GetDataComponent.DATA_TYPE_INTEGER,
                GetDataComponent.SOURCE_TYPE_GET_CLIENT_VALUE,
                screenId,
                componentId,
                GetDataComponent.IS_REQUIRED_FALSE,
                0);
    }

    public static GetDataComponent createGetSourceTypeServerDefault(int screenId, int componentId) {
        return new GetDataComponent(
                GetDataComponent.DATA_TYPE_INTEGER,
                GetDataComponent.SOURCE_TYPE_GET_SERVER_VALUE,
                screenId,
                componentId,
                GetDataComponent.IS_REQUIRED_FALSE, 0);
    }

}
