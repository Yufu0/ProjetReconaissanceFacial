package reconnaissancefacial;

import java.io.IOException;
import java.util.HashMap;

public class FaceRecognition {
    public FaceRecognition(String imageSrc) throws IOException {
        /* ouverture de l'image et modification au format souhaité */

        MySQL mysql = MySQL.getInstance();

        ImageProcessing faceToRecognize = new ImageProcessing(imageSrc);
        faceToRecognize.toBlackAndWhite();
        faceToRecognize.resize(Main.WIDTH, Main.HEIGHT);
        /* convertion de l'image en un vecteur */
        Vector vectorToRecognize = faceToRecognize.toMatrix().toVector();

        /* recuperation des eigenfaces dans la bdd */
        Matrix eigenfaces = mysql.getEigenfaces();

        /* projection du vecteur de l'image sur l'espace des eigenfaces */
        Vector vectorToRecognizeProjected = eigenfaces.projectVector(vectorToRecognize);

        /* recuperation des coordonnées des visages dans l'espace des eigen faces */
        HashMap<Integer, Vector> faces = mysql.getProjectedFaces();

        /* comparaison avec tous les visages pour determiner le plus proche */
        int closestId = -1;
        double distance = -1;
        for (int id : faces.keySet()) {
            double newDistance = vectorToRecognizeProjected.compareTo(faces.get(id));
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
