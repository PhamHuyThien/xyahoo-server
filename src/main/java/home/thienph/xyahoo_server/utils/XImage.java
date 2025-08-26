package home.thienph.xyahoo_server.utils;

import home.thienph.xyahoo_server.data.resources.ImageInfo;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class XImage {
    @SneakyThrows
    public static ImageInfo parseImageInfo(Path imagePath) {
        File file = imagePath.toFile();
        byte[] fileData = Files.readAllBytes(imagePath);
        BufferedImage image = ImageIO.read(file);
        return new ImageInfo(
                file.getName(),
                file.getAbsolutePath(),
                file.length(),
                image.getWidth(),
                image.getHeight(),
                fileData
        );
    }

    public static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }
}
