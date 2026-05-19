import java.awt.Color;
import java.awt.image.BufferedImage;

public class GrayConverter {

    public BufferedImage convert(BufferedImage original) {
        if (original == null) return null;

        int width = original.getWidth();
        int height = original.getHeight();
        
        // 建立一張新的空白圖片來儲存灰階結果，設定為 TYPE_INT_ARGB 支援透明度
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 雙層迴圈走訪圖片的每一個像素 (Pixel)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 取得原始像素的顏色
                Color color = new Color(original.getRGB(x, y), true);
                
                // 依照人眼視覺感知的加權公式計算灰階值
                int grayValue = (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
                
                // 確保灰階值落在 0~255 的安全範圍內
                grayValue = Math.min(255, Math.max(0, grayValue));
                
                // 使用計算出的灰階值建立新的顏色 (R, G, B 數值相同即為灰階)
                Color grayColor = new Color(grayValue, grayValue, grayValue, color.getAlpha());
                
                // 將新顏色填入新圖片的對應位置
                grayImage.setRGB(x, y, grayColor.getRGB());
            }
        }
        
        return grayImage;
    }
}