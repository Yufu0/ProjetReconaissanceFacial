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
            vectorsImages[i] = (new ImageProcessing(images.get(id))).toMatrix().toVector();
            i++;
        }
        

        /* convertion du tableau de vecteurs en une matrice */
        Matrix matrixImages = new Matrix(vectorsImages);

        /* calcule de l'ACP sur cette matrice */
        ACP acp = new ACP(matrixImages);

        /* stockage des eigenfaces dans la bdd */
        for(double val:acp.getEigenVectors().keySet()) {
            mysql.addEigenface(acp.getEigenVectors().get(val),val);
        }

        /* stockage du visage moyen dans la bdd */
        // mysql.saveFaceMean(acp.getVectorMean());

        /* on projettent tous les vecteurs sur l'espace des eigenfaces */
        HashMap<Integer, Vector> vectorsImagesProjected = new HashMap<>();
        for (int idImage : images.keySet()) {
            Vector vector = (new ImageProcessing(images.get(idImage)).toMatrix().toVector());
            Vector vectorProjected = acp.getEigenMatrix().projectVector(vector);
            vectorsImagesProjected.put(idImage, vectorProjected);
        }

        /* on stock les projections des vecteurs dans la bdd (on clear aussi ceux précédamant calculé) */
        for(int id:vectorsImagesProjected.keySet()) {
            mysql.addProjectedFace(vectorsImagesProjected.get(id),id);
        }
    }
}
