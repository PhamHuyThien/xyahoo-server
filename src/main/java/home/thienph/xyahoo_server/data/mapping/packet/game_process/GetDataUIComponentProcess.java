package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.data.resources.GetDataComponent;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetDataUIComponentProcess implements IGameProcessPacketPipeline {
    int requestId;
    List<GetDataComponent> components;

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(3);
        payload.writeInt(requestId);
        payload.writeInt(components.size());
        for (GetDataComponent component : components) {
            int datType = component.getDataType();
            int sourceType = component.getSourceType();
            payload.writeByte(datType);
            payload.writeByte(sourceType);
            if (datType == 0) {
                if (sourceType == 0)
                    payload.writeInt((int) component.getDefaultValue());
                else if (sourceType == 1) {
                    payload.writeInt(component.getScreenId());
                    payload.writeInt(component.getComponentId());
                    payload.writeBoolean(component.isRequired());
                }
            } else if (datType == 1) {
                if (sourceType == 0)
                    XByteBuf.writeString(payload, String.valueOf(component.getDefaultValue()));
                else if (sourceType == 1) {
                    payload.writeInt(component.getScreenId());
                    payload.writeInt(component.getComponentId());
                    payload.writeBoolean(component.isRequired());
                }
            } else if (datType == 2) {
                if (sourceType == 0)
                    payload.writeBoolean((boolean) component.getDefaultValue());
                else if (sourceType == 1) {
                    payload.writeInt(component.getScreenId());
                    payload.writeInt(component.getComponentId());
                }
            } else if (datType == 3 && sourceType == 1) {
                payload.writeInt(component.getScreenId());
                payload.writeInt(component.getComponentId());
            }
        }
    }
}
