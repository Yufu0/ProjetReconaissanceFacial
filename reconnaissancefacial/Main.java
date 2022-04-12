package reconnaissancefacial;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        ImageProcessing eee0 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_0.jpg");
        ImageProcessing eee1 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_1.jpg");
        ImageProcessing eee2 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_2.jpg");
        ImageProcessing eee3 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_3.jpg");
        ImageProcessing eee4 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_4.jpg");
        ImageProcessing eee5 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_5.jpg");

        eee0.toBlackAndWhite();
        eee1.toBlackAndWhite();
        eee2.toBlackAndWhite();
        eee3.toBlackAndWhite();
        eee4.toBlackAndWhite();
        eee5.toBlackAndWhite();



        eee0.resize(1000,1000);
        eee1.resize(1000,1000);
        eee2.resize(1000,1000);
        eee3.resize(1000,1000);
        eee4.resize(1000,1000);
        eee5.resize(1000,1000);

        System.out.println("ez");

        Vector[] v = new Vector[6];

        v[0] = eee0.toMatrix().toVector();
        v[1] = eee1.toMatrix().toVector();
        v[2] = eee2.toMatrix().toVector();
        v[3] = eee3.toMatrix().toVector();
        v[4] = eee4.toMatrix().toVector();
        v[5] = eee5.toMatrix().toVector();


        Matrix mat = new Matrix(v);
        ACP acp = new ACP(mat);

        Vector[] v2 = new Vector[6];
        for (int i = 0; i < 6; i++) {
            v2[i] = acp.getEigenMatrix().projectVector(v[i]);
        }
        System.out.println("Imagine t'arrive ici");
        System.out.println(new Matrix(v2));

        System.out.println("juste pour troll");

    }

}
