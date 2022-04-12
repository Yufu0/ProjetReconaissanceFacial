package reconnaissancefacial;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageProcessing {

    private int[][][] imageTableau;

    public ImageProcessing(String source) throws IOException {

        BufferedImage img = ImageIO.read(new File(source));
        int width = img.getWidth(null);
        int height = img.getHeight(null);


        this.imageTableau = new int[width][height][3];

        Raster tramPixelsImg = img.getRaster();

        int[] rgb;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb = tramPixelsImg.getPixel(i, j, (int[]) null);
                this.imageTableau[i][j] = new int[] {rgb[0], rgb[1], rgb[2]};
            }
        }
    }

    public void afficher() {
        for (int[][] a : this.imageTableau) {
            System.out.print("\n");
            for (int[] b : a) {
                if(b.length == 3) System.out.print(b[0] + ";" + b[1] + ";" + b[2] + " ");
                if(b.length == 1) System.out.print(b[0] + " ");
            }
        }
    }

    public void toBlackAndWhite() {
        if (this.imageTableau.length == 0 || this.imageTableau[0].length == 0 || this.imageTableau[0][0].length != 3) return;
        for (int i = 0; i < this.imageTableau.length; i++) {
            for (int j = 0; j < this.imageTableau[i].length; j++) {
                this.imageTableau[i][j] = new int[] {(int) Math.floor(0.2126 * (double) this.imageTableau[i][j][0] + 0.7152 * (double) this.imageTableau[i][j][1] + 0.0722 * (double) this.imageTableau[i][j][2])};
            }
        }
    }

    public void resize(int width, int height) {
        if (this.imageTableau.length < width || this.imageTableau[0].length < height || this.imageTableau[0][0].length != 1) return;

        int[][][] newImageTableau = new int[width][height][1];

        int saveK;
        int saveL;
        int compteur;
        int somme;
        int k = 0;
        int l = 0;

        for (int i = 0; i < width; i++) {
            saveK = k;
            l=0;
            for (int j = 0; j < height; j++) {
                compteur = 0;
                somme = 0;

                k = saveK;
                saveL = l;
                while (k * width / this.imageTableau.length == i) {
                    l = saveL;
                    while (l * height / this.imageTableau[k].length == j) {
                        somme += this.imageTableau[k][l][0];
                        compteur++;
                        l++;
                    }
                    k++;
                }

                if (compteur != 0 && somme != 0) newImageTableau[i][j] = new int[]{somme / compteur};
            }
        }
        this.imageTableau = newImageTableau;
    }

    public Matrix toMatrix(){
        if (this.imageTableau.length == 0 || this.imageTableau[0].length == 0 || this.imageTableau[0][0].length != 1) throw new RuntimeException("Oof c'est pas en N&B");
        Matrix m = new Matrix(this.imageTableau.length, this.imageTableau[0].length);
        for (int i = 0; i < this.imageTableau.length; i++) {
            for (int j = 0; j < this.imageTableau[i].length; j++) {
                m.set(i,j, (double)this.imageTableau[i][j][0]);
            }
        }

        return m;
    }

    public static void saveResult(Matrix matrix, String fileName) throws IOException {
        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        double[] arrayDouble;
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                arrayDouble = new double[]{matrix.get(i, j)};
                raster.setPixel(j, i, arrayDouble);
            }
        }

        ImageIO.write(image, "PNG", new File(fileName));
    }
}
