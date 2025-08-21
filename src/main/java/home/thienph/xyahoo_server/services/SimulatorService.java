package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.friends.ContactGroup;
import home.thienph.xyahoo_server.data.friends.ContactInfo;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulatorService {

    public void danhSachBanTLMN(ChannelHandlerContext ctx) {
        Packet packet = new Packet(5000009);
        ByteBuf payload = packet.getPayload();

        List<ContactInfo> contactInfos = new ArrayList<>();
        contactInfos.add(new ContactInfo("CongCkuaBeB0ng", "bbContact1user1", 1, "Online", true, new int[]{1, 2}, 4, 1, 1, 1));
        contactInfos.add(new ContactInfo("HoangTuCuaE", "bbContact1user1", 1, "Online", false, new int[]{1, 2}, 4, 1, 1, 1));
        contactInfos.add(new ContactInfo("Song Thanh", "bbContact1user1", 2, "Online", true, new int[]{1, 2}, 4, 1, 1, 1));
        contactInfos.add(new ContactInfo("Pham Huy Thien", "bbContact1user2", 2, "Online", true, new int[]{1, 2}, 4, 1, 1, 1));

        payload.writeInt(contactInfos.size());
        for (ContactInfo contactInfo : contactInfos) {
            XByteBuf.writeString(payload, contactInfo.getDisplayName());
            XByteBuf.writeString(payload, contactInfo.getContactId());
            XByteBuf.writeString(payload, contactInfo.getDisplayName());
            payload.writeInt(contactInfo.getRoomId());
        }

        ctx.writeAndFlush(packet);

    }
    public void handleGameChat(ChannelHandlerContext ctx) {
        Packet packet = new Packet(3411);
        XByteBuf.writeString(packet.getPayload(), "");
        XByteBuf.writeString(packet.getPayload(), "123");
        XByteBuf.writeString(packet.getPayload(), "132443");
        ctx.writeAndFlush(packet);
    }

    public void listFriend(ChannelHandlerContext ctx) {
        Packet packet = new Packet(1045);
        ByteBuf payload = packet.getPayload();

        List<ContactInfo> contactInfos = new ArrayList<>();
        contactInfos.add(new ContactInfo("CongCkuaBeB0ng", "bbContact1user1", 1, "Online", true, new int[]{1, 2}, 4, 1, 1, 1));
        contactInfos.add(new ContactInfo("HoangTuCuaE", "bbContact1user1", 1, "Online", false, new int[]{1, 2}, 4, 1, 1, 1));
        contactInfos.add(new ContactInfo("Song Thanh", "bbContact1user1", 2, "Online", true, new int[]{1, 2}, 4, 1, 1, 1));
        contactInfos.add(new ContactInfo("Pham Huy Thien", "bbContact1user2", 2, "Online", true, new int[]{1, 2}, 4, 1, 1, 1));


        payload.writeInt(1);
        payload.writeInt(contactInfos.size());
        for (ContactInfo contactInfo : contactInfos) {
            XByteBuf.writeString(payload, contactInfo.getDisplayName());
            XByteBuf.writeString(payload, contactInfo.getContactId());
            payload.writeShort(contactInfo.getRoomId());
        }

        ctx.writeAndFlush(packet);
    }

    public void listFriendYahoo(ChannelHandlerContext ctx) {
        List<ContactGroup> contactGroups = new ArrayList<>();
        contactGroups.add(new ContactGroup("Bạn bè"));
        contactGroups.add(new ContactGroup("Nhóm chat"));
        contactGroups.add(new ContactGroup("Chuyên gia"));

        contactGroups.get(0).contacts.add(new ContactInfo("bbContact1user1", "bbContact1user1", 2, "Online", true, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(0).contacts.add(new ContactInfo("bbContact1user2", "bbContact1user2", 2, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(0).contacts.add(new ContactInfo("bbContact1user3", "bbContact1user3", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(0).contacts.add(new ContactInfo("bbContact1user4", "bbContact1user4", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));

        contactGroups.get(1).contacts.add(new ContactInfo("bbContact2user1", "bbContact2user1", 1, "Online", true, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(1).contacts.add(new ContactInfo("bbContact2user2", "bbContact2user2", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(1).contacts.add(new ContactInfo("bbContact2user3", "bbContact2user3", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(1).contacts.add(new ContactInfo("bbContact2user4", "bbContact2user4", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));

        contactGroups.get(2).contacts.add(new ContactInfo("bbContact3user1", "bbContact3user1", 1, "Online", true, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(2).contacts.add(new ContactInfo("bbContact3user2", "bbContact3user2", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(2).contacts.add(new ContactInfo("bbContact3user3", "bbContact3user3", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));
        contactGroups.get(2).contacts.add(new ContactInfo("bbContact3user4", "bbContact3user4", 1, "Online", false, new int[]{1, 2}, 1, 1, 1, 1));

        Packet packet = new Packet(6, 2);
        ByteBuf payload = packet.getPayload();

        payload.writeInt(contactGroups.size());
        for (ContactGroup contactGroup : contactGroups) {
            XByteBuf.writeString(payload, contactGroup.getGroupName());
            payload.writeInt(contactGroup.contacts.size());
            for (ContactInfo contactInfo : contactGroup.contacts) {
                XByteBuf.writeString(payload, contactInfo.contactId);
                payload.writeInt(contactInfo.statusCode);
                XByteBuf.writeString(payload, contactInfo.statusMessage);
                XByteBuf.writeString(payload, "");
                XByteBuf.writeString(payload, contactInfo.displayName);
                XByteBuf.writeString(payload, "");
            }
        }
        ctx.writeAndFlush(packet);

    }

    public void loginYahooSuccess(ChannelHandlerContext ctx) {
        Packet packet = new Packet(-5, 2);
        ctx.writeAndFlush(packet);
    }

    public void moiVaoPhongChat(ChannelHandlerContext ctx) {
        Packet packet = new Packet(1006);
        XByteBuf.writeString(packet.getPayload(), "0001");
        XByteBuf.writeString(packet.getPayload(), "0002");
        XByteBuf.writeString(packet.getPayload(), "0003");
        ctx.writeAndFlush(packet);
    }

    public void buzz(ChannelHandlerContext ctx) {
        Packet packet = new Packet(27);
        XByteBuf.writeString(packet.getPayload(), "X Yahoo!");
        XByteBuf.writeString(packet.getPayload(), "X Yahoo!");
        ctx.writeAndFlush(packet);
    }


    public void moiVaoPhong(ChannelHandlerContext ctx) {
        Packet packet = new Packet(1001);
        XByteBuf.writeString(packet.getPayload(), "0001");
        XByteBuf.writeString(packet.getPayload(), "Phòng 1");
        ctx.writeAndFlush(packet);
    }

    public void outputLoginSuccess(ChannelHandlerContext ctx) {
        Packet packet = new Packet(-5, 1);
        ctx.writeAndFlush(packet);
    }

    public void showPopupDialog(ChannelHandlerContext ctx) {
        Packet packet = new Packet(29);

        ByteBuf payload = Unpooled.buffer();

        // btn left
        XByteBuf.writeString(payload, "menu");
        // data btn left
        ByteBuf btnLeft = Unpooled.buffer();
        btnLeft.writeInt(4);
        XByteBuf.writeString(btnLeft, "String 1");
        btnLeft.writeInt(1);
        btnLeft.writeBoolean(true);
        XByteBuf.writeByteArray(payload, btnLeft.array());

        //btn center
        XByteBuf.writeString(payload, "OK");
        // data btn center
        XByteBuf.writeByteArray(payload, new byte[0]);

        //btn right
        XByteBuf.writeString(payload, "Cancel");
        // data btn right
        XByteBuf.writeByteArray(payload, new byte[0]);

        packet.setPayload(payload);
        ctx.writeAndFlush(packet);
    }
}
