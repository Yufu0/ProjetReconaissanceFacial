package reconnaissancefacial;

import java.io.IOException;
import java.util.HashMap;

public class FaceRecognition {
    public FaceRecognition(String imageSrc) throws IOException {
        /* ouverture de l'image et modification au format souhaité */

        MySQL mysql = MySQL.getInstance();

        ImageProcessing faceToRecognize = new ImageProcessing(imageSrc, "COULEUR");

        /* convertion de l'image en un vecteur */
        Vector vectorToRecognize = faceToRecognize.toMatrix().toVector();



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

        System.out.println("av acp");
        /* calcule de l'ACP sur cette matrice */
        ACP acp = new ACP(matrixImages);
        System.out.println("ap acp");

        /* projection du vecteur de l'image sur l'espace des eigenfaces */
        Vector vectorToRecognizeProjected = acp.getEigenMatrix().projectVector(vectorToRecognize);






        /* on projettent tous les vecteurs sur l'espace des eigenfaces */
        HashMap<Integer, Vector> faces = new HashMap<>();
        for (int idImage : images.keySet()) {
            Vector vector = (new ImageProcessing(images.get(idImage), "N&B").toMatrix().toVector());
            Vector vectorProjected = acp.getEigenMatrix().projectVector(vector);
            faces.put(idImage, vectorProjected);
        }

        /* comparaison avec tous les visages pour determiner le plus proche */
        int closestId = -1;
        double distance = -1;
        for (int id : faces.keySet()) {
            double newDistance = vectorToRecognizeProjected.compareTo(faces.get(id));
            System.out.println(newDistance);
            if (distance < 0 || distance > newDistance) {
                distance = newDistance;
                closestId = id;
            }
        }


        /* affichage du resultat */
        if (closestId == -1 || distance > Main.EPSILON) {
            System.out.println("Le visage est inconnu");
        } else {
            /* on vas chercher le nom de la personne grace a son id */
            String name = mysql.getName(closestId);
            System.out.println("Visage trouvé, c'est le visage de " + name + " !");
        }
    }
}
