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
        ImageProcessing eee7 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_7.jpg");
        ImageProcessing eee8 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_8.jpg");
        ImageProcessing eee9 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_9.jpg");
        ImageProcessing eee10 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_10.jpg");
        ImageProcessing eee11 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_11.jpg");



        eee0.toBlackAndWhite();
        eee1.toBlackAndWhite();
        eee2.toBlackAndWhite();
        eee3.toBlackAndWhite();
        eee4.toBlackAndWhite();
        eee5.toBlackAndWhite();
        eee6.toBlackAndWhite();
        eee7.toBlackAndWhite();
        eee8.toBlackAndWhite();
        eee9.toBlackAndWhite();
        eee10.toBlackAndWhite();
        eee11.toBlackAndWhite();



        eee0.resize(1000,1000);
        eee1.resize(1000,1000);
        eee2.resize(1000,1000);
        eee3.resize(1000,1000);
        eee4.resize(1000,1000);
        eee5.resize(1000,1000);
        eee6.resize(1000,1000);
        eee7.resize(1000,1000);
        eee8.resize(1000,1000);
        eee9.resize(1000,1000);
        eee10.resize(1000,1000);
        eee11.resize(1000,1000);

        System.out.println("ez");

        Vector[] v = new Vector[12];

        v[0] = eee0.toMatrix().toVector();
        v[1] = eee1.toMatrix().toVector();
        v[2] = eee2.toMatrix().toVector();
        v[3] = eee3.toMatrix().toVector();
        v[4] = eee4.toMatrix().toVector();
        v[5] = eee5.toMatrix().toVector();
        v[6] = eee6.toMatrix().toVector();
        v[7] = eee7.toMatrix().toVector();
        v[8] = eee8.toMatrix().toVector();
        v[9] = eee9.toMatrix().toVector();
        v[10] = eee10.toMatrix().toVector();
        v[11] = eee11.toMatrix().toVector();


        Matrix mat = new Matrix(v);
        ACP acp = new ACP(mat);

        Vector[] v2 = new Vector[12];
        for (int i = 0; i < 12; i++) {
            v2[i] = acp.getEigenMatrix().projectVector(v[i]);
        }
        System.out.println("Imagine t'arrive ici");
        //System.out.println(new Matrix(v2));
        System.out.println(eee11.toMatrix().compareTo(eee11.toMatrix()));

        Matrix matrix = new Matrix(1000,1000);
        System.out.println(matrix.compareTo(eee11.toMatrix()));
        //matrix = matrix.add(acp.getVectorMean().toMatrix(1000));
        System.out.println(matrix.compareTo(eee11.toMatrix()));
        for (int i = 0; i < 1; i++) {
            matrix = matrix.add(acp.getEigenMatrix().getVectors()[i].toMatrix(1000).multiplyByConstant(10000));
            System.out.println(matrix.compareTo(eee11.toMatrix()));
        }
        ImageProcessing.saveResult(matrix, "resultat.png");
        System.out.println(matrix.compareTo(eee11.toMatrix()));
    }

}
