package home.thienph.xyahoo_server.managers;

import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.resources.Grid;
import home.thienph.xyahoo_server.data.users.UserContext;
import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
@Slf4j
public class GameManager {
    private final Map<Channel, UserContext> userContexts = new ConcurrentHashMap<>();
    private final List<Grid> homeGrids = new ArrayList<>();

    @PostConstruct
    public void init() {
        log.info("Init game manager...");
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
        log.info("Init game manager done.");
    }

    public Channel getChannelByUsername(String username) {
        Map.Entry<Channel, UserContext> userContext = userContexts.entrySet().stream()
                .filter(entry -> username.equals(entry.getValue().getUsername()))
                .findFirst()
                .orElse(null);
        return userContext == null ? null : userContext.getKey();
    }

    public UserContext getUserContext(Channel channel) {
        return userContexts.get(channel);
    }


}
