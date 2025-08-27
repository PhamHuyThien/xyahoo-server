package home.thienph.xyahoo_server.data.users;

import home.thienph.xyahoo_server.constants.RoomConst;
import home.thienph.xyahoo_server.entities.RoomEntity;
import home.thienph.xyahoo_server.entities.RoomGroupEntity;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.Set;

@Data
public class RoomContext {
    RoomGroupEntity roomGroup;
    RoomEntity room;
    Set<Channel> channels;
    Set<UserContext> users;
    int roomStatus;
    String roomStatusText;
    byte[] icon;


    public void update() {
        roomStatus = calculateRoomStatus(channels.size(), room.getMaxUser());
        roomStatusText = channels.size() + "/" + room.getMaxUser();
    }

    private int calculateRoomStatus(int current, int max) {
        if (max <= 0) return 0; // tránh chia 0
        double ratio = (double) current / max;
        if (ratio < 0.4) {
            return RoomConst.ROOM_STATUS_LOW; // ít người
        } else if (ratio < 0.7) {
            return RoomConst.ROOM_STATUS_MEDIUM; // trung bình
        } else {
            return RoomConst.ROOM_STATUS_BIG; // nhiều người
        }
    }

}