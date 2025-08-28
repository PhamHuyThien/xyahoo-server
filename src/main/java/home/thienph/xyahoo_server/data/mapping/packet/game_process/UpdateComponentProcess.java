package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.data.mapping.IPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component.AUpdateComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class UpdateComponentProcess implements IGameProcessPacketPipeline, IPipeline<AUpdateComponent, UpdateComponentProcess> {
    int screenId;
    List<AUpdateComponent> components = new ArrayList<>();

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(62);
        payload.writeInt(screenId);
        payload.writeByte(components.size());
        for (AUpdateComponent component : components) {
            payload.writeInt(component.getComponentId());
            payload.writeInt(component.getComponentType());
            component.build(payload);
        }
    }


    @Override
    public UpdateComponentProcess addPipeline(AUpdateComponent data) {
        components.add(data);
        return this;
    }

    @Override
    public UpdateComponentProcess addPipeline(IPipelineGroup<AUpdateComponent> function) {
        components.add(function.group());
        return this;
    }

    @Override
    public UpdateComponentProcess endPipeline() {
        return this;
    }

    @Override
    public void flushPipeline(Channel channel) {
    }
}
