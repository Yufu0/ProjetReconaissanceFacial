package reconnaissancefacial;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {
    private int[][] imageTableau;

    public ImageProcessing(String source, String type) throws IOException {
        BufferedImage img = ImageIO.read(new File(source));

        if (type == "COULEUR") {
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            // crée l'image de sortie
            BufferedImage newImg = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);

            // balancer l'image d'entrée à l'image de sortie
            Graphics2D g = newImg.createGraphics();
            g.drawImage(img, 0, 0, Main.WIDTH, Main.HEIGHT, 0, 0, width, height,null);
            g.dispose();

            this.imageTableau = new int[Main.WIDTH][Main.HEIGHT];

            Raster tramPixelsImg = newImg.getRaster();

            int[] rgb;
            for (int i = 0; i < Main.WIDTH; i++) {
                for (int j = 0; j < Main.HEIGHT; j++) {
                    rgb = tramPixelsImg.getPixel(i, j, (int[]) null);
                    this.imageTableau[i][j] = (int) Math.floor(0.2126 * (double) rgb[0] + 0.7152 * (double) rgb[1] + 0.0722 * (double) rgb[2]);
                }
            }
        } else if (type == "N&B") {
            this.imageTableau = new int[Main.WIDTH][Main.HEIGHT];

            Raster tramPixelsImg = img.getRaster();

            int[] b;
            for (int i = 0; i < Main.WIDTH; i++) {
                for (int j = 0; j < Main.HEIGHT; j++) {
                    b = tramPixelsImg.getPixel(i, j, (int[]) null);
                    this.imageTableau[i][j] = b[0];
                }
            }
        }
    }


    public Matrix toMatrix(){
        if (this.imageTableau.length == 0 || this.imageTableau[0].length == 0);
        Matrix m = new Matrix(this.imageTableau.length, this.imageTableau[0].length);
        for (int i = 0; i < this.imageTableau.length; i++) {
            for (int j = 0; j < this.imageTableau[i].length; j++) {
                m.set(i,j, (double) this.imageTableau[i][j]);
            }
        }

        return m;
    }
    public void save(String source) throws IOException {
        BufferedImage image = new BufferedImage(this.imageTableau.length, this.imageTableau[0].length, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        int[] arrayInt;
        for (int i = 0; i < this.imageTableau.length; i++) {
            for (int j = 0; j < this.imageTableau[0].length; j++) {
                arrayInt = new int[]{this.imageTableau[i][j]};
                if(this.imageTableau[i][j] > 255) arrayInt = new int[]{255};
                if (this.imageTableau[i][j] < 0) arrayInt = new int[]{0};
                raster.setPixel(i, j, arrayInt);
            }
        }
        ImageIO.write(image, "jpg", new File(source));
    }
}
