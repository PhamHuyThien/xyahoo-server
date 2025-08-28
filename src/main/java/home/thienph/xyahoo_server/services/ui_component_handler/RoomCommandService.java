package home.thienph.xyahoo_server.services.ui_component_handler;

import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.RoomEntity;
import home.thienph.xyahoo_server.entities.RoomGroupEntity;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.repositories.RoomGroupRepo;
import home.thienph.xyahoo_server.repositories.RoomRepo;
import home.thienph.xyahoo_server.utils.XPacket;
import home.thienph.xyahoo_server.utils.XString;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomCommandService {
    @Autowired
    GameManager gameManager;
    @Autowired
    RoomGroupRepo roomGroupRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    HomeCommandService homeCommandService;

    public void roomCreateNewRoom(Channel channel, String roomName) {
        UserContext userContext = gameManager.getUserContext(channel);
        RoomGroupEntity roomGroupEntity = roomGroupRepo.findFirstByIsForUser(1).orElse(null);
        if (roomGroupEntity == null) {
            XPacket.showSimpleDialog(channel, "Không thể tạo phòng chat mới");
            return;
        }
        if (roomRepo.existsByRoomName(roomName)) {
            XPacket.showSimpleDialog(channel, "Tên phòng này đã có người đặt rồi");
            return;
        }

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomName(roomName);
        roomEntity.setUserOwnerId(userContext.getId());
        roomEntity.setMaxUser(20);
        roomEntity.setRoomKey(XString.replaceSpaceWithUnderscore(XString.removeVietnameseDiacritics(roomName)));
        roomEntity.setIconId(104);
        roomEntity.setCreateAt(new Date());
        roomEntity.setUserOwnerId(userContext.getId());
        roomEntity.setRoomId(roomGroupEntity.getId());
        roomRepo.save(roomEntity);

        gameManager.loadAllRoomContexts();
        homeCommandService.homeSelectRoom(channel, null);
    }
}
