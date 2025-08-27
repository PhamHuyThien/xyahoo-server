package home.thienph.xyahoo_server.data.users;

import home.thienph.xyahoo_server.entities.GameResourceEntity;
import lombok.Data;

@Data
public class ResourceContext {
    GameResourceEntity resourceEntity;
    byte[] resourceData;
}
