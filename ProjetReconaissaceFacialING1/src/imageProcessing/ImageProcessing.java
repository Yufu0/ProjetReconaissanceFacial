package imageProcessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {
    public static void imageToBW(String srcImage) throws IOException {
        Image img = ImageIO.read(new File(srcImage));
        int height = img.getHeight(null);
        int width = img.getWidth(null);

        BufferedImage bufferImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferImgNB = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        Graphics graphics = bufferImg.createGraphics();

        graphics.drawImage(img, 0, 0, null);

        Raster tramPixelsImg = bufferImg.getRaster();
        WritableRaster tramPixelsImgNB = bufferImgNB.getRaster();

        int[] res = new int[1];
        int[] rgb;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb = tramPixelsImg.getPixel(i, j, (int[]) null);
                res[0] = (int) Math.floor(0.2126 * (double) rgb[0] + 0.7152 * (double) rgb[1] + 0.0722 * (double) rgb[2]);
                tramPixelsImgNB.setPixel(i, j, res);
            }
        }
        ImageIO.write(bufferImgNB, "PNG", new File("img/result.png"));
    }


    public static void resize(String srcImage, int width, int height) throws IOException {
        Image img = ImageIO.read(new File(srcImage));
        int previousWidth = img.getWidth(null);
        int previousHeight = img.getHeight(null);

        BufferedImage bufferImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = bufferImg.createGraphics();
        graphics.drawImage(img, 0, 0, width, height, null);

        ImageIO.write(bufferImg, "PNG", new File("img/result.png"));
    }

    public static void main(String[] args) throws IOException {
        resize("img/img1.png", 100, 100);
        imageToBW("img/result.png");
    }
}
