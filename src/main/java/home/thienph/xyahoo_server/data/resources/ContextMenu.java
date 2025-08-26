package home.thienph.xyahoo_server.data.resources;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContextMenu {
    String label;
    GameProcessPacketPipeline action;
}
