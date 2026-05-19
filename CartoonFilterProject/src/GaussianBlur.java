import java.awt.Color;
import java.awt.image.BufferedImage;

public class GaussianBlur {

    public BufferedImage apply(BufferedImage image) {
        if (image == null) return null;
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage blurredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 5x5 高斯模糊權重矩陣 (能有效抹平毛孔與雜訊)
        int[][] kernel = {
            {1,  4,  7,  4, 1},
            {4, 16, 26, 16, 4},
            {7, 26, 41, 26, 7},
            {4, 16, 26, 16, 4},
            {1,  4,  7,  4, 1}
        };
        int weightSum = 273; // 矩陣權重總和

        // 避開最外圍 2 個像素避免越界
        for (int y = 2; y < height - 2; y++) {
            for (int x = 2; x < width - 2; x++) {
                int r = 0, g = 0, b = 0;

                // 套用 5x5 矩陣
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        Color color = new Color(image.getRGB(x + j, y + i));
                        int weight = kernel[i + 2][j + 2];
                        r += color.getRed() * weight;
                        g += color.getGreen() * weight;
                        b += color.getBlue() * weight;
                    }
                }
                
                Color newColor = new Color(r / weightSum, g / weightSum, b / weightSum);
                blurredImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return blurredImage;
    }
}