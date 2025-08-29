package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.mapping.PacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.CacheImagePacket;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ResourceService {

    @Autowired
    GameManager gameManager;

    @SneakyThrows
    public void loadResource(UserContext userContext) {
        PacketPipeline loadImageCachePipeline = PacketPipeline.newInstance();
        gameManager.getResourceContexts().forEach(resourceContext
                -> loadImageCachePipeline.addPipeline(new CacheImagePacket(
                resourceContext.getResourceEntity().getResourceId(),
                resourceContext.getResourceData())));
        loadImageCachePipeline.endPipeline().flushPipeline(userContext);
    }
}
