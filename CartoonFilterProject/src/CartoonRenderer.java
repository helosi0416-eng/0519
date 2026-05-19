import java.awt.Color;
import java.awt.image.BufferedImage;

public class CartoonRenderer {

    public BufferedImage render(BufferedImage quantizedImage, BufferedImage edgeImage) {
        if (quantizedImage == null || edgeImage == null) return null;

        int width = quantizedImage.getWidth();
        int height = quantizedImage.getHeight();
        BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 分別取得兩張圖在同一個位置的像素顏色
                Color qColor = new Color(quantizedImage.getRGB(x, y), true);
                Color eColor = new Color(edgeImage.getRGB(x, y), true);

                // 計算邊緣圖的亮度比例 (0.0 代表最黑的線條，1.0 代表最白的平坦區)
                // 因為邊緣圖是灰階的，R=G=B，我們直接取 Red 來計算即可
                float edgeFactor = eColor.getRed() / 255.0f;

                // 將色塊的 RGB 值乘上邊緣比例
                // 如果遇到黑色線條 (edgeFactor 為 0)，算出來的顏色就會變成 0 (黑色)
                int r = (int) (qColor.getRed() * edgeFactor);
                int g = (int) (qColor.getGreen() * edgeFactor);
                int b = (int) (qColor.getBlue() * edgeFactor);

                Color finalColor = new Color(r, g, b, qColor.getAlpha());
                finalImage.setRGB(x, y, finalColor.getRGB());
            }
        }
        return finalImage;
    }
}