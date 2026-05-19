import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class MainApp extends JFrame {
    
    
    private JLabel imageLabel;
    private BufferedImage originalImage;
    private BufferedImage currentImage;
    
    private ImageLoader imageLoader;
    private GrayConverter grayConverter;
    private EdgeDetector edgeDetector;
    private ColorQuantizer colorQuantizer;
    private CartoonRenderer cartoonRenderer; // 新增卡通渲染器
    private GaussianBlur gaussianBlur;

    public MainApp() {
        imageLoader = new ImageLoader();
        grayConverter = new GrayConverter();
        edgeDetector = new EdgeDetector();
        colorQuantizer = new ColorQuantizer();
        cartoonRenderer = new CartoonRenderer(); // 實例化
        gaussianBlur = new GaussianBlur();
        
        setTitle("傳統影像處理：卡通濾鏡 App");
        setSize(900, 650); // 稍微加寬視窗以容納所有按鈕
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        imageLabel = new JLabel("請載入一張圖片", SwingConstants.CENTER);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        // 控制面板
        JPanel controlPanel = new JPanel();
        JButton loadButton = new JButton("載入");
        JButton grayButton = new JButton("灰階");
        JButton edgeButton = new JButton("邊緣");
        JButton quantizeButton = new JButton("色塊");
        JButton cartoonButton = new JButton("✨ 生成卡通 ✨"); // 終極按鈕
        JButton saveButton = new JButton("💾 儲存結果");    // 儲存按鈕
        
        loadButton.addActionListener(e -> chooseAndLoadImage());
        grayButton.addActionListener(e -> applyGrayScale());
        edgeButton.addActionListener(e -> applyEdgeDetection());
        quantizeButton.addActionListener(e -> applyColorQuantization());
        cartoonButton.addActionListener(e -> applyCartoonFilter()); // 綁定生成事件
        saveButton.addActionListener(e -> saveCurrentImage());      // 綁定儲存事件
        
        controlPanel.add(loadButton);
        controlPanel.add(grayButton);
        controlPanel.add(edgeButton);
        controlPanel.add(quantizeButton);
        controlPanel.add(cartoonButton);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL)); // 分隔線
        controlPanel.add(saveButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void chooseAndLoadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            originalImage = imageLoader.load(selectedFile.getAbsolutePath());
            currentImage = originalImage;
            updateImageDisplay();
        }
    }

    private void applyGrayScale() {
        if (originalImage != null) {
            currentImage = grayConverter.convert(originalImage);
            updateImageDisplay();
        }
    }

    private void applyEdgeDetection() {
        if (originalImage != null) {
            BufferedImage gray = grayConverter.convert(originalImage);
            currentImage = edgeDetector.detect(gray);
            updateImageDisplay();
        }
    }

    private void applyColorQuantization() {
        if (originalImage != null) {
            currentImage = colorQuantizer.quantize(originalImage);
            updateImageDisplay();
        }
    }

    // 新增：一鍵執行完整的 Cartoon Pipeline
  private void applyCartoonFilter() {
    if (originalImage != null) {
        // 步驟 0: 高斯模糊 (先抹平細節)
        BufferedImage blurred = gaussianBlur.apply(originalImage);
        
        // 為了讓卡通感更重，我們連續套用兩次模糊！
        blurred = gaussianBlur.apply(blurred); 

        // 步驟 1 & 2: 用模糊過的圖轉灰階 -> 找邊緣 (這樣線條才會乾淨)
        BufferedImage gray = grayConverter.convert(blurred);
        BufferedImage edges = edgeDetector.detect(gray);
        
        // 步驟 3: 用模糊過的圖去取色塊 (顏色才不會有一粒一粒的雜訊)
        BufferedImage quantized = colorQuantizer.quantize(blurred);
        
        // 步驟 4: 合併渲染
        currentImage = cartoonRenderer.render(quantized, edges);
        updateImageDisplay();
    } else {
        JOptionPane.showMessageDialog(this, "請先載入圖片！");
    }
}

    // 新增：將目前畫面上的結果存檔
    private void saveCurrentImage() {
        if (currentImage != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("儲存圖片");
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                // 如果使用者沒有輸入副檔名，自動補上 .png
                if (!saveFile.getName().toLowerCase().endsWith(".png")) {
                    saveFile = new File(saveFile.getAbsolutePath() + ".png");
                }
                try {
                    ImageIO.write(currentImage, "png", saveFile);
                    JOptionPane.showMessageDialog(this, "儲存成功！");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "儲存失敗：" + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "沒有可以儲存的圖片！");
        }
    }

    private void updateImageDisplay() {
        if (currentImage != null) {
            imageLabel.setIcon(new ImageIcon(currentImage));
            imageLabel.setText(""); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}