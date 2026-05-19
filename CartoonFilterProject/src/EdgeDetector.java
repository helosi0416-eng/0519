import java.awt.Color;
import java.awt.image.BufferedImage;

public class EdgeDetector {

    public BufferedImage detect(BufferedImage grayImage) {
        if (grayImage == null) return null;

        int width = grayImage.getWidth();
        int height = grayImage.getHeight();
        // 已經修正此行的語法錯誤
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[][] gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] gy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = 0;
                int pixelY = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int val = new Color(grayImage.getRGB(x + j, y + i)).getRed();
                        pixelX += val * gx[i + 1][j + 1];
                        pixelY += val * gy[i + 1][j + 1];
                    }
                }

                int magnitude = (int) Math.min(255, Math.sqrt(pixelX * pixelX + pixelY * pixelY));

                int threshold = 60; 
                int edgeVal = (magnitude > threshold) ? 0 : 255;

                Color edgeColor = new Color(edgeVal, edgeVal, edgeVal, 255);
                edgeImage.setRGB(x, y, edgeColor.getRGB());
            }
        }
        return edgeImage;
    }
}