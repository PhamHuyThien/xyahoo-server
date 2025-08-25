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
        homeGrids.add(new Grid("+4 Phương", 2, 101, false));
        homeGrids.add(new Grid("Yahoo!", ScreenConstant.YAHOO_SCREEN_ID, 102, false));
        homeGrids.add(new Grid("Hồ Sơ", 4, 103, false));
        homeGrids.add(new Grid("Tiền", 4, 103, false));
        homeGrids.add(new Grid("Shop", 4, 103, false));
        homeGrids.add(new Grid("Tiến Lên", 4, 103, false));
        homeGrids.add(new Grid("Media", 4, 103, false));
        homeGrids.add(new Grid("Giải trí", 4, 103, false));
        homeGrids.add(new Grid("Top", 4, 103, false));
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
