package reconnaissancefacial;

import java.io.IOException;
import java.util.HashMap;

public class ImageDatabaseComputation {
    public static void compute() throws IOException {

        MySQL mysql = MySQL.getInstance();

        /* recuperation des images depuis la bdd {idImage => source} */
        HashMap<Integer, String> images = mysql.getImages();

        /* convertion des images en un vecteurs */
        int i = 0;
        Vector[] vectorsImages = new Vector[images.size()];
        for (int id : images.keySet()) {
            vectorsImages[i] = (new ImageProcessing(images.get(id), "N&B")).toMatrix().toVector();
            i++;
        }

        /* convertion du tableau de vecteurs en une matrice */
        Matrix matrixImages = new Matrix(vectorsImages);


        /* calcule de l'ACP sur cette matrice */
        ACP acp = new ACP(matrixImages, Init.NUMBER_EIGENFACES);

        /* stockage du visage moyen dans la bdd */
        mysql.saveEigenFaces(acp.getEigenMatrix());
    }
}
