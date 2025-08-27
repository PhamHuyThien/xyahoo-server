package home.thienph.xyahoo_server.managers;

import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.resources.Grid;
import home.thienph.xyahoo_server.data.users.ResourceContext;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.GameResourceEntity;
import home.thienph.xyahoo_server.entities.RoomEntity;
import home.thienph.xyahoo_server.entities.RoomGroupEntity;
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
    private final List<Grid> homeGrids = new ArrayList<>();
    private final List<RoomContext> roomContexts = new ArrayList<>();
    private final List<ResourceContext> resourceContexts = new ArrayList<>();

    @Autowired
    RoomGroupRepo roomGroupRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    GameResourceRepo gameResourceRepo;


    @PostConstruct
    public void init() {
        log.info("Init game manager...");

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
        log.info("Done load resources, total: {}", resourceContexts.size());

        homeGrids.add(new Grid("Bạn Bè", ScreenConstant.FRIEND_SCREEN_ID, 100, false));
        homeGrids.add(new Grid("+4 Phương", ScreenConstant.ROOM_SCREEN_ID, 101, false));
        homeGrids.add(new Grid("Yahoo!", ScreenConstant.YAHOO_SCREEN_ID, 102, false));
        homeGrids.add(new Grid("Hồ Sơ", ScreenConstant.PROFILE_SCREEN_ID, 103, false));
        homeGrids.add(new Grid("Tiền", ScreenConstant.MONEY_SCREEN_ID, 103, false));
        homeGrids.add(new Grid("Shop", ScreenConstant.SHOP_SCREEN_ID, 103, false));
        homeGrids.add(new Grid("Tiến Lên", ScreenConstant.GAME_SCREEN_ID, 103, false));
        homeGrids.add(new Grid("Media", ScreenConstant.MEDIA_SCREEN_ID, 103, false));
        homeGrids.add(new Grid("Giải trí", ScreenConstant.ENTERTAINMENT_SCREEN_ID, 103, false));
        homeGrids.add(new Grid("Top", ScreenConstant.TOP_SCREEN_ID, 103, false));
        log.info("Done load home grids.");

        List<RoomEntity> rooms = roomRepo.findAllByExpireAtIsNullOrExpireAtAfter(new Date());
        for (RoomEntity room : rooms) {
            RoomGroupEntity roomGroup = roomGroupRepo.findById(room.getRoomId()).orElse(null);
            if (roomGroup == null) continue;
            RoomContext roomContext = new RoomContext();
            roomContext.setRoomGroup(roomGroup);
            roomContext.setRoom(room);
            roomContext.setChannels(new ArrayList<>());
            roomContext.setUsers(new ArrayList<>());
            roomContext.setIcon(getResourceContextById(room.getIconId()).getResourceData());
            roomContext.update();
            roomContexts.add(roomContext);
        }
        log.info("Done load rooms, total: {}", roomContexts.size());

        log.info("Init game manager done.");
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
