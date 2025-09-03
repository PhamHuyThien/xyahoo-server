package home.thienph.xyahoo_server.data.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageOffline {
    long userId;
    String username;
    String timestamp;
    String message;
}
