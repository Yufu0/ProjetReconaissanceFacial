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
        ImageProcessing eee6 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_6.jpg");

        eee0.toBlackAndWhite();
        eee1.toBlackAndWhite();
        eee2.toBlackAndWhite();
        eee3.toBlackAndWhite();
        eee4.toBlackAndWhite();
        eee5.toBlackAndWhite();
        eee6.toBlackAndWhite();

        eee0.resize(100,100);
        eee1.resize(100,100);
        eee2.resize(100,100);
        eee3.resize(100,100);
        eee4.resize(100,100);
        eee5.resize(100,100);
        eee6.resize(100,100);

        Vector[] v = new Vector[7];

        v[0] = eee0.toMatrix().toVector();
        v[1] = eee1.toMatrix().toVector();
        v[2] = eee2.toMatrix().toVector();
        v[3] = eee3.toMatrix().toVector();
        v[4] = eee4.toMatrix().toVector();
        v[5] = eee5.toMatrix().toVector();
        v[6] = eee6.toMatrix().toVector();


        Matrix mat = new Matrix(v);
        ACP acp = new ACP(mat);
        acp.compute();

        Vector[] v2 = new Vector[7];
        for (int i = 0; i < 7; i++) {
            v2[i] = acp.getMatrixEigenVector().projectVector(v[i]);
        }
        System.out.println("Imagine t'arrive ici");
//        System.out.println(new Matrix(v2));

/*

        Vector[] vects = new Vector[4];
        vects[0] = new Vector(new double[]{1, 2, 3});
        vects[1] = new Vector(new double[]{7, 1, 3});
        vects[2] = new Vector(new double[]{0, 2, 5});
        vects[3] = new Vector(new double[]{8, 8, 3});
        Matrix mat = new Matrix(vects);


        ACP acp = new ACP(mat);
        acp.compute();


        Vector[] vects2 = new Vector[4];
        for (int i = 0; i < 4; i++) {
            vects2[i] = acp.getMatrixEigenVector().projectVector(vects[i]);
        }
        System.out.println(new Matrix(vects2));
*/
    }

}
