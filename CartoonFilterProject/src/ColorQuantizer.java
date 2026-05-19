import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorQuantizer {
    
    // 設定色階數量 (改為 6 讓色彩過渡稍微順一點，但依然有色塊感)
    private int levels = 6;

    public BufferedImage quantize(BufferedImage image) {
        if (image == null) return null;

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage quantizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int step = 255 / (levels - 1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y), true);

                // 【關鍵修改】：RGB 轉 HSB 來提升飽和度與亮度
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                
                // 提升飽和度 (乘以 1.5 倍)，最高不超過 1.0
                hsb[1] = Math.min(1.0f, hsb[1] * 1.5f); 
                // 提升亮度 (乘以 1.2 倍)，最高不超過 1.0
                hsb[2] = Math.min(1.0f, hsb[2] * 1.2f); 
                
                // 轉換回 RGB 色彩
                Color boostedColor = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));

                // 接著用提升過鮮豔度的顏色來進行量化
                int r = Math.round((float) boostedColor.getRed() / step) * step;
                int g = Math.round((float) boostedColor.getGreen() / step) * step;
                int b = Math.round((float) boostedColor.getBlue() / step) * step;

                r = Math.min(255, r);
                g = Math.min(255, g);
                b = Math.min(255, b);

                Color newColor = new Color(r, g, b, color.getAlpha());
                quantizedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return quantizedImage;
    }
}