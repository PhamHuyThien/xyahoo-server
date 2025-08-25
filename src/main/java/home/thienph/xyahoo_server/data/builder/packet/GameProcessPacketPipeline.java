package home.thienph.xyahoo_server.data.builder.packet;

import home.thienph.xyahoo_server.data.builder.APacketPipeline;
import home.thienph.xyahoo_server.data.builder.IPipelineGroup;
import home.thienph.xyahoo_server.data.builder.packet.game_process.IGameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameProcessPacketPipeline extends APacketPipeline {
    ByteBuf payloadPipeline = Unpooled.buffer();
    List<IGameProcessPacketPipeline> gameProcessPipelines = new ArrayList<>();

    public GameProcessPacketPipeline() {
        super(121);
    }

    public static GameProcessPacketPipeline newInstance() {
        return new GameProcessPacketPipeline();
    }

    public GameProcessPacketPipeline addPipeline(IGameProcessPacketPipeline gameProcess) {
        gameProcessPipelines.add(gameProcess);
        return this;
    }

    public GameProcessPacketPipeline addPipeline(IPipelineGroup<IGameProcessPacketPipeline> pipelineGroup) {
        gameProcessPipelines.add(pipelineGroup.groupPipeline());
        return this;
    }

    public GameProcessPacketPipeline endPipeline() {
        gameProcessPipelines.forEach(gameProcess -> gameProcess.build(payloadPipeline));
        payloadPipeline.writeInt(2);
        return this;
    }

    @Override
    public GameProcessPacketPipeline build() {
        XByteBuf.writeByteArray(packet.getPayload(), payloadPipeline.array());
        return this;
    }
}
