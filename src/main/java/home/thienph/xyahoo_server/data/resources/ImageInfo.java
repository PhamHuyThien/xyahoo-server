package home.thienph.xyahoo_server.data.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfo {
    String name;
    String absolutePath;
    long size;
    int width;
    int height;
    byte[] data;
}
