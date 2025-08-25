package home.thienph.xyahoo_server.data.builder.packet.game_process;

import home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component.AComponent;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UIComponentProcess implements IGameProcessPacketPipeline {
    int dialogId;
    int componentId;
    AComponent component;

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(6);
        payload.writeInt(dialogId);
        payload.writeByte(component.getComponentType());
        component.build(payload);
        payload.writeInt(componentId);
    }
}
