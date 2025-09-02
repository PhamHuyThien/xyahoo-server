package home.thienph.xyahoo_server.managers;

import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.mapping.packet.UpdateUserIsOnlinePacket;
import home.thienph.xyahoo_server.data.users.ResourceContext;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.*;
import home.thienph.xyahoo_server.repositories.*;
import home.thienph.xyahoo_server.utils.XImage;
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
    private final Map<String, UserContext> userContexts = new ConcurrentHashMap<>();
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
    @Autowired
    UserFriendRepo userFriendRepo;


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
        List<RoomEntity> rooms = roomRepo.findAllRoomOpen(new Date());
        for (RoomEntity room : rooms) {
            RoomGroupEntity roomGroup = roomGroupRepo.findById(room.getRoomGroupId()).orElse(null);
            if (roomGroup == null) continue;
            RoomContext roomContext = new RoomContext();
            roomContext.setRoomGroup(roomGroup);
            roomContext.setRoom(room);
            roomContext.setUsers(new HashSet<>());
            roomContext.setIcon(getResourceContextById(room.getIconId()).getResourceData());
            roomContext.update();
            roomContexts.add(roomContext);
        }
        this.roomContexts.clear();
        this.roomContexts.addAll(roomContexts);
        log.info("Done load rooms, total: {}", this.roomContexts.size());
    }

    public UserContext getUserContextByChannelId(String channelId) {
        return userContexts.get(channelId);
    }

    public UserContext getUserContextByUserId(Long userId) {
        return userContexts.values().stream()
                .filter(userContext -> userId.equals(userContext.getUserId()))
                .findFirst().orElse(null);
    }

    public UserContext getUserContextByUsername(String username) {
        return userContexts.values().stream()
                .filter(userContext -> username.equals(userContext.getUsername()))
                .findFirst().orElse(null);
    }

    public ResourceContext getResourceContextById(int resourceId) {
        return resourceContexts.stream()
                .filter(resourceContext -> resourceId == resourceContext.getResourceEntity().getResourceId())
                .findFirst().orElse(null);
    }

    public RoomContext getRoomContextByRoomKey(String roomKey) {
        return roomContexts.stream()
                .filter(roomContext -> roomContext.getRoom().getRoomKey().equals(roomKey))
                .findFirst().orElse(null);
    }

    public void destroySessionUserByChannelId(String channelId) {
        UserContext userContext = getUserContexts().get(channelId);
        showMessageOnOfflineForFriends(userContext, UserConstant.TYPE_STATUS_OFFLINE);
        userContext.destroy();
        userContexts.remove(channelId);
        roomContexts.forEach(roomContext -> {
            if (roomContext.getUsers().remove(userContext))
                roomContext.update();
        });
    }

    public void showMessageOnOfflineForFriends(UserContext userContext, int typeStatus) {
        if(userContext.getUser().getShowOnline() == 1){
            List<UserEntity> listFriend = userFriendRepo.findAllUserFriendByUsername(userContext.getUsername());
            listFriend.forEach(friend -> {
                UserContext friendContext = getUserContextByUserId(friend.getId());
                if (friendContext != null) {
                    new UpdateUserIsOnlinePacket(userContext.getUserId(), typeStatus).build().flush(friendContext);
                }
            });
        }
    }
}
