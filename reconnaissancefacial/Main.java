package reconnaissancefacial;


import java.io.IOException;

public class Main {
    public static final int WIDTH = 200;
    public static final int HEIGHT = 300;
    public static final int EPSILON = 10;

    public static void main(String[] args) throws IOException {

        final MySQL database =  MySQL.getInstance();
        database.connexion();

        database.deconnexion();

        ImageProcessing eee0 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_0.jpg");
        ImageProcessing eee1 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_1.jpg");
        ImageProcessing eee2 = new ImageProcessing("img/Base_Images_Apprentissage/AXEL_LANTA/AXEL_LANTA_2.jpg");


        eee0.toBlackAndWhite();
        eee1.toBlackAndWhite();
        eee2.toBlackAndWhite();

        eee0.resize(1000,1000);
        eee1.resize(1000,1000);
        eee2.resize(1000,1000);

        System.out.println("ez");

        Vector[] v = new Vector[3];

        v[0] = eee0.toMatrix().toVector();
        v[1] = eee1.toMatrix().toVector();
        v[2] = eee2.toMatrix().toVector();



        Matrix mat = new Matrix(v);

        ACP acp = new ACP(mat);

        Vector[] v2 = new Vector[3];
        for (int i = 0; i < 3; i++) {
            v2[i] = acp.getEigenMatrix().projectVector(v[i]);
        }
        System.out.println("Imagine t'arrive ici");


        Matrix matrix = new Matrix(1000,1000);

        matrix = matrix.add(acp.getVectorMean().toMatrix(1000));

        for (int i = 0; i < 3; i++) {
            matrix = matrix.add(acp.getEigenMatrix().getVectors()[i].toMatrix(1000).multiplyByConstant(v2[1].get(i)));

        }
        ImageProcessing.saveResult(matrix, "resultat.png");


    }

}
