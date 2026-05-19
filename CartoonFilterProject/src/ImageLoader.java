import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
    
    // 讀取圖片的方法
    public BufferedImage load(String filePath) {
        try {
            File file = new File(filePath);
            return ImageIO.read(file);
        } catch (IOException e) {
            System.out.println("讀取圖片失敗: " + e.getMessage());
            return null;
        }
    }
    
    // 之後可以把儲存圖片的方法也寫在這裡
}