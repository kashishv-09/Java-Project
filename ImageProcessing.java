import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ImageProcessing {

    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Do you want to add watermark to the image? (yes/no)");
            String watermarkOption = scanner.nextLine();
            if (watermarkOption.equalsIgnoreCase("yes")) {
                System.out.println("Enter the path of the input image:");
                String inputPath = scanner.nextLine();
                String watermarkText = "";
                addWatermark(inputPath, watermarkText);

            }

            System.out.println("Do you want to compress the image? (yes/no)");
            String compressionOption = scanner.nextLine();
            if (compressionOption.equalsIgnoreCase("yes")) {
                System.out.println("Enter the path of the input image:");
                String inputPath1 = scanner.nextLine();
                compressImage(inputPath1);

            }

            System.out.println("Do you want to decompress the image? (yes/no)");
            String decompressionOption = scanner.nextLine();
            if (decompressionOption.equalsIgnoreCase("yes")) {
                System.out.println("Enter the path of the input image:");
                String inputPath2 = scanner.nextLine();
                decompressImage(inputPath2);
            }
        }
    }

    public static long addWatermark(String inputPath, String watermarkText) throws IOException {
        // Load the original image
        File inputImageFile = new File(inputPath);
        BufferedImage inputImage = ImageIO.read(inputImageFile);

        // Print the size of the original image
        System.out.println("Original image size: " + inputImageFile.length() + " bytes");

        // Create a new BufferedImage to hold the watermarked image
        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // Get the Graphics2D object to draw on the output image
        Graphics2D graphics = outputImage.createGraphics();

        // Draw the original image onto the output image
        graphics.drawImage(inputImage, 0, 0, null);

        // Set the font and color for the watermark text
        Font font = new Font("Arial", Font.BOLD, 36);
        Color color = new Color(255, 255, 255, 128);
        graphics.setFont(font);
        graphics.setColor(color);

        // Add the watermark text to the output image
        // String watermarkText = "Watermarked";
        int x = outputImage.getWidth() / 2 - graphics.getFontMetrics().stringWidth(watermarkText) / 2;
        int y = outputImage.getHeight() / 2;
        graphics.drawString(watermarkText, x, y);

        // Save the watermarked image to disk
        File outputImageFile = new File("watermarked.jpg");
        ImageIO.write(outputImage, "jpg", outputImageFile);

        // Print the size of the watermarked image
        System.out.println("Watermarked image size: " + outputImageFile.length() + " bytes");

        System.out.println("Watermark added to the image.");

        return outputImageFile.length();
    }

    public static long compressImage(String inputPath) throws IOException {
        File input = new File(inputPath);
        BufferedImage image = ImageIO.read(input);

        // Print the size of the original image
        System.out.println("Original image size before compression: " + input.length() + " bytes");

        // compress the image
        File output = new File("compressed.jpg");
        float quality = 0.5f;
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(output);
        writer.setOutput(ios);

        JPEGImageWriteParam param = new JPEGImageWriteParam(null);
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        writer.write(null, new javax.imageio.IIOImage(image, null, null), param);

        ios.flush();
        writer.dispose();

        // Print the size of the compressed image
        System.out.println("Compressed image size: " + output.length() + " bytes");

        System.out.println("Image compression completed.");

        return output.length();

    }

    public static long decompressImage(String inputPath) throws IOException {
        File input = new File(inputPath);

        // create an ImageInputStream from the compressed image file
        ImageInputStream inputStream = ImageIO.createImageInputStream(input);

        // Print the size of the original image
        System.out.println("Original image size before decompression: " + input.length() + " bytes");

        // get an ImageReader for the JPEG format
        ImageReader reader = ImageIO.getImageReadersByFormatName("jpeg").next();
        reader.setInput(inputStream);

        // read the compressed image and decompress it into a BufferedImage
        BufferedImage image = reader.read(0);

        // set the output file format and compression quality
        File output = new File("decompressed.jpg");
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam param = new JPEGImageWriteParam(null);
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(1.0f);

        // write the decompressed image to a new file
        writer.setOutput(new FileImageOutputStream(output));
        writer.write(null, new javax.imageio.IIOImage(image, null, null), param);

        // close the input and output streams
        inputStream.close();
        writer.dispose();

        // Print the size of the decompressed image
        System.out.println("Decompressed image size: " + output.length() + " bytes");

        System.out.println("Image decompression completed.");

        return output.length();
    }
}