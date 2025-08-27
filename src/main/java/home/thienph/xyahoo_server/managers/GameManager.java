package home.thienph.xyahoo_server.managers;

import home.thienph.xyahoo_server.data.users.ResourceContext;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.GameHomeEntity;
import home.thienph.xyahoo_server.entities.GameResourceEntity;
import home.thienph.xyahoo_server.entities.RoomEntity;
import home.thienph.xyahoo_server.entities.RoomGroupEntity;
import home.thienph.xyahoo_server.repositories.GameHomeRepo;
import home.thienph.xyahoo_server.repositories.GameResourceRepo;
import home.thienph.xyahoo_server.repositories.RoomGroupRepo;
import home.thienph.xyahoo_server.repositories.RoomRepo;
import home.thienph.xyahoo_server.utils.XImage;
import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
@Slf4j
public class GameManager {
    private final Map<Channel, UserContext> userContexts = new ConcurrentHashMap<>();
    private final List<GameHomeEntity> gameHomes = new ArrayList<>();
    private final List<RoomContext> roomContexts = new ArrayList<>();
    private final List<ResourceContext> resourceContexts = new ArrayList<>();

    @Autowired
    RoomGroupRepo roomGroupRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    GameResourceRepo gameResourceRepo;
    @Autowired
    GameHomeRepo gameHomeRepo;


    @PostConstruct
    public void init() {
        log.info("Init game manager...");
        loadGameResources();
        loadGameHomes();
        loadAllRoomContexts();
        log.info("Init game manager done.");
    }

    public void loadGameResources() {
        List<ResourceContext> resourceContexts = new ArrayList<>();
        List<GameResourceEntity> gameResources = gameResourceRepo.findAllByStatus(1);
        for (GameResourceEntity gameResource : gameResources) {
            byte[] resourceData = XImage.readAllBytes(Path.of(gameResource.getResourceFile()));
            if (resourceData == null) {
                log.error("Game resource {} has no data", gameResource.getResourceId());
                continue;
            }
            ResourceContext resourceContext = new ResourceContext();
            resourceContext.setResourceEntity(gameResource);
            resourceContext.setResourceData(resourceData);
            resourceContexts.add(resourceContext);
        }
        this.resourceContexts.clear();
        this.resourceContexts.addAll(resourceContexts);
        log.info("Done load resources, total: {}", this.resourceContexts.size());
    }

    public void loadGameHomes() {
        List<GameHomeEntity> gameHomes = gameHomeRepo.findAllByStatusOrderByOrderAsc(1);
        this.gameHomes.clear();
        this.gameHomes.addAll(gameHomes);
        log.info("Done load  game home, total: {}", this.gameHomes.size());
    }

    public void loadAllRoomContexts() {
        List<RoomContext> roomContexts = new ArrayList<>();
        List<RoomEntity> rooms = roomRepo.findAllByExpireAtIsNullOrExpireAtAfter(new Date());
        for (RoomEntity room : rooms) {
            RoomGroupEntity roomGroup = roomGroupRepo.findById(room.getRoomId()).orElse(null);
            if (roomGroup == null) continue;
            RoomContext roomContext = new RoomContext();
            roomContext.setRoomGroup(roomGroup);
            roomContext.setRoom(room);
            roomContext.setChannels(new HashSet<>());
            roomContext.setUsers(new HashSet<>());
            roomContext.setIcon(getResourceContextById(room.getIconId()).getResourceData());
            roomContext.update();
            roomContexts.add(roomContext);
        }
        this.roomContexts.clear();
        this.roomContexts.addAll(roomContexts);
        log.info("Done load rooms, total: {}", this.roomContexts.size());
    }

    public Optional<Channel> getOptionalChannelByUsername(String username) {
        return Optional.ofNullable(getChannelByUsername(username));
    }

    public Channel getChannelByUsername(String username) {
        Map.Entry<Channel, UserContext> userContext = userContexts.entrySet().stream()
                .filter(entry -> username.equals(entry.getValue().getUsername()))
                .findFirst()
                .orElse(null);
        return userContext == null ? null : userContext.getKey();
    }

    public Channel getChannelByUserId(long userId) {
        Map.Entry<Channel, UserContext> userContext = userContexts.entrySet().stream()
                .filter(entry -> userId == entry.getValue().getId())
                .findFirst()
                .orElse(null);
        return userContext == null ? null : userContext.getKey();
    }

    public UserContext getUserContext(Channel channel) {
        return userContexts.get(channel);
    }

    public ResourceContext getResourceContextById(int resourceId) {
        return resourceContexts.stream()
                .filter(resourceContext -> resourceId == resourceContext.getResourceEntity().getResourceId())
                .findFirst().orElse(null);
    }

    public RoomContext getRoomContextByRoomKey(String roomKey) {
        return roomContexts.stream().filter(roomContext -> roomContext.getRoom().getRoomKey().equals(roomKey)).findFirst().orElse(null);
    }
}
