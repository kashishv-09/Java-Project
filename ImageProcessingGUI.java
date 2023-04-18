import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageProcessingGUI extends JFrame implements ActionListener {

    private JButton addWatermarkButton;
    private JButton compressImageButton;
    private JButton decompressImageButton;
    private JLabel beforeLabel;
    private JLabel afterLabel;

    public ImageProcessingGUI() {
        super("Image Processing");

        addWatermarkButton = new JButton("Add Watermark");
        compressImageButton = new JButton("Compress Image");
        decompressImageButton = new JButton("Decompress Image");
        beforeLabel = new JLabel("Before: No Image uploaded yet!");
        afterLabel = new JLabel("After: No Image uploaded yet!");

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(addWatermarkButton);
        panel.add(compressImageButton);
        panel.add(decompressImageButton);
        panel.add(beforeLabel);
        panel.add(afterLabel);

        add(panel);

        addWatermarkButton.addActionListener(this);
        compressImageButton.addActionListener(this);
        decompressImageButton.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new ImageProcessingGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image");

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String inputPath = fileChooser.getSelectedFile().getAbsolutePath();

            try {
                // Get size of image before operation
                long beforeSize = Files.size(Path.of(inputPath));
                beforeLabel.setText("Before: Image size before operation is " + beforeSize + " bytes");

                if (e.getSource() == addWatermarkButton) {
                    // Prompt user to enter watermark text
                    String watermarkText = JOptionPane.showInputDialog(this, "Enter watermark text:");

                    // Get size of image after operation
                    long afterSize = ImageProcessing.addWatermark(inputPath, watermarkText);

                    afterLabel.setText("After: Image size after adding Watermark to image is " + afterSize + " bytes");

                    JOptionPane.showMessageDialog(this, "Watermark added successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (e.getSource() == compressImageButton) {
                    // Get size of image after operation
                    long afterSize = ImageProcessing.compressImage(inputPath);

                    afterLabel.setText("After: Image size after the Compression of image is " + afterSize + " bytes");

                    JOptionPane.showMessageDialog(this, "Image compressed successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (e.getSource() == decompressImageButton) {
                    // Get size of image after operation
                    long afterSize = ImageProcessing.decompressImage(inputPath);

                    afterLabel.setText("After: Image size after the Decompression of image is " + afterSize + " bytes");

                    JOptionPane.showMessageDialog(this, "Image decompressed successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error processing image", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}