package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.data.mapping.IPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.IGameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameProcessPacketPipeline extends APacketPipeline implements IPipeline<IGameProcessPacketPipeline, GameProcessPacketPipeline> {
    ByteBuf payloadPipeline = Unpooled.buffer();
    List<IGameProcessPacketPipeline> gameProcessPipelines = new ArrayList<>();
    boolean isEndPipeline = false;
    boolean isBuild = false;

    public GameProcessPacketPipeline() {
        super(121);
    }

    public static GameProcessPacketPipeline newInstance() {
        return new GameProcessPacketPipeline();
    }

    public GameProcessPacketPipeline addPipeline(IGameProcessPacketPipeline data) {
        gameProcessPipelines.add(data);
        return this;
    }

    public GameProcessPacketPipeline addPipeline(IPipelineGroup<IGameProcessPacketPipeline> function) {
        gameProcessPipelines.add(function.group());
        return this;
    }

    public GameProcessPacketPipeline endPipeline() {
        if (!isEndPipeline) {
            isEndPipeline = true;
            gameProcessPipelines.forEach(gameProcess -> gameProcess.build(payloadPipeline));
            payloadPipeline.writeInt(2);
        }
        return this;
    }

    @Override
    public void flushPipeline(UserContext userContext) {
        userContext.getChannel().writeAndFlush(packet);
    }

    @Override
    public GameProcessPacketPipeline build() {
        if (!isBuild) {
            isBuild = true;
            XByteBuf.writeByteArray(packet.getPayload(), payloadPipeline.array());
        }
        return this;
    }
}
