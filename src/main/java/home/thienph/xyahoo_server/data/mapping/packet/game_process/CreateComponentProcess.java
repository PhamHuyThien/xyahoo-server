package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component.ACreateComponent;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateComponentProcess implements IGameProcessPacketPipeline {
    int dialogId;
    int componentId;
    ACreateComponent component;

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(6);
        payload.writeInt(dialogId);
        payload.writeByte(component.getComponentType());
        component.build(payload);
        payload.writeInt(componentId);
    }
}
