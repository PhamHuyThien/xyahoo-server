package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.mapping.PacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.NewDialogProcess;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class SimulatorService {



    public void loadCache(ChannelHandlerContext ctx) {
        Packet packet = new Packet(122);
        packet.getPayload().writeInt(100);
        packet.getPayload().writeInt(1);
        ctx.writeAndFlush(packet);
    }

    @SneakyThrows
    public void processData(Channel ctx) {
//        loadCache(ctx);

//        cacheImageData(ctx);
//        Thread.sleep(100000000);

        PacketPipeline packetPipeline = new PacketPipeline();


        GameProcessPacketPipeline gameProcessPacketPipeline = GameProcessPacketPipeline.newInstance();
        gameProcessPacketPipeline.addPipeline(new NewDialogProcess("Màn hình chính", 1111, true));

//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 1, new TextInputCreateComponent("Nhap ten cua ban", 20, 20, (byte) 0)));
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 2, new TextCreateComponent("Phạm huy Thiên", 200, 16726823, (byte) 0)));
//        gameProcessPacketPipeline.addPipeline(new FocusComponentProcess(11111, 1));
//
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 4, new SimpleTextCreateComponent("cháu lên 3 chasu di mau giao co thuong chau vi chau hay khoc nhe, hay khoc nhe de me trong cay trai", 0)));
//
//        GameProcessPacketPipeline textLinkAction = GameProcessPacketPipeline.newInstance()
//                .addPipeline(new DialogWrapTextProcess("Phạm huy thiên dz "));
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 3, new TextLinkCreateComponent(" bấm vào đây", 200, 16726823, textLinkAction)));
//
//        ImageInfo imageInfo = XImage.parseImageInfo(Path.of("data/images/Icn1.png"));
//        GameProcessPacketPipeline imageAction = GameProcessPacketPipeline.newInstance()
//                .addPipeline(new DialogWrapTextProcess("Phạm huy thiên dz "));
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 5, new ImageSourceCreateComponent(100, imageInfo.getWidth(), imageInfo.getHeight(), true, true, "bam vao day", imageAction, (byte) 5)));

//        GameProcessPipeline imageAction = GameProcessPipeline.newInstance()
//                        .addPipeline(new DialogWrapTextProcess("Phạm huy thiên dz "));
//        byte[] images = Files.readAllBytes(Path.of("data/images/Icn1.png"));
//        gameProcessPipeline.addPipeline(new CreateComponentProcess(11111, 5, new ImageDataCreateComponent(images, 200, 200, true, true, "bam vao day", imageAction, (byte) 0)));

//        GameProcessPacketPipeline dropDownAction = GameProcessPacketPipeline.newInstance()
//                .addPipeline(new DialogWrapTextProcess("Phạm huy thiên dz change"));
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 6, new DropdownCreateComponent("dropdown đẹp không", 0, List.of("lua chon 1", "lua chon 2", "lua chon 3"), dropDownAction, 0, (byte) 0)));

//        gameProcessPipeline.addPipeline(new CreateComponentProcess(11111, 4, new SimpleTextCreateComponent("cháu lên 3 chasu di mau giao co thuong chau vi chau hay khoc nhe, hay khoc nhe de me trong cay trai", 0)));

//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 7, new MultiImageCreateComponent(List.of(100, 101, 102), imageInfo.getWidth(), imageInfo.getHeight(), true, true, "click name", GameProcessPacketPipeline.newInstance(), (byte) 0)));
//
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 8, new LabelCreateComponent("labelday nhe", 16726823, (byte) 0)));

//        List<GridInfo> grids = new ArrayList<>();
//        grids.add(new GridInfo("grid 1", 1, 100, false));
//        grids.add(new GridInfo("grid 2", 2, 101, false));
//        grids.add(new GridInfo("grid 3", 3, 102, false));
//        grids.add(new GridInfo("grid 4", 4, 103, false));
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 8, new GridCreateComponent(40, 40, false, grids, GameProcessPacketPipeline.newInstance())));

//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 9, new CheckBoxCreateComponent("checked hihi", false, (byte) 0)));
//
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 10, new PopupDialogCreateComponent("popup day nhe", (byte) 0, GameProcessPacketPipeline.newInstance())));
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 10, new ButtonCreateComponent("buttone", 100, GameProcessPacketPipeline.newInstance())));

//        List<BuddyInfo> buddyInfoList = new ArrayList<>();
//        byte[] images = Files.readAllBytes(Path.of("data/images/Stat1.png"));
//        buddyInfoList.add(new BuddyInfo("grA", "displayA", "mediaExtension A", "dltext1", 100, images, 2, "description A"));
//        buddyInfoList.add(new BuddyInfo("grA", "displayB", "mediaExtension B", "dltext2", 100, images, 0, "description B"));
//        buddyInfoList.add(new BuddyInfo("grC", "displayC", "mediaExtension C", "dltext3", 100, images, 3, "description C"));
//        buddyInfoList.add(new BuddyInfo("grD", "displayD", "mediaExtension D", "dltext4", 100, images, 1, "description D"));
//        gameProcessPacketPipeline.addPipeline(new CreateComponentProcess(11111, 11, new ListCreateComponent(true, (byte) 0, (byte) 3, 10, 10, (byte) 1, buddyInfoList, GameProcessPacketPipeline.newInstance())));

        gameProcessPacketPipeline.endPipeline();

        packetPipeline.addPipeline(gameProcessPacketPipeline);
        packetPipeline.endPipeline();

        packetPipeline.flushPipeline(ctx);
    }
}
