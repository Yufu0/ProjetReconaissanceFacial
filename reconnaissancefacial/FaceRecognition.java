package reconnaissancefacial;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class FaceRecognition {
    private String firstNameIdentified = "?";
    private String lastNameIdentified = "?";


    private String closestImage = "img/inconnu.png";

    public String getClosestImage() {
        return closestImage;
    }

    public String getFirstNameIdentified() {
        return firstNameIdentified;
    }

    public String getLastNameIdentified() {
        return lastNameIdentified;
    }


    public FaceRecognition(String imageSrc) throws IOException, SQLException, ClassNotFoundException {
        /* ouverture de l'image et modification au format souhaité */

        reconnaissancefacial.MySQL mysql = reconnaissancefacial.MySQL.getInstance();
        mysql.connexion();

        reconnaissancefacial.ImageProcessing faceToRecognize = new reconnaissancefacial.ImageProcessing(imageSrc, "COULEUR");

        /* convertion de l'image en un vecteur */
        Vector vectorToRecognize = faceToRecognize.toMatrix().toVector();

        /* recuperation des images depuis la bdd {idImage => source} */
        HashMap<Integer, String> images = mysql.getImages();

        /* convertion des images en un vecteurs */
        int i = 0;
        Vector[] vectorsImages = new Vector[images.size()];
        for (int id : images.keySet()) {
            vectorsImages[i] = (new reconnaissancefacial.ImageProcessing(images.get(id), "N&B")).toMatrix().toVector();
            i++;
        }

        /* convertion du tableau de vecteurs en une matrice */
        Matrix matrixImages = new Matrix(vectorsImages);

        System.out.println("Calcul en cours...");

        /* calcule de l'ACP sur cette matrice */
        Matrix matrixEigenFaces = mysql.getEigenFaces();

        /* projection du vecteur de l'image sur l'espace des eigenfaces */
        Vector vectorToRecognizeProjected = matrixEigenFaces.projectVector(vectorToRecognize);

        /* on projettent tous les vecteurs sur l'espace des eigenfaces */
        HashMap<Integer, Vector> faces = new HashMap<>();
        for (int idImage : images.keySet()) {
            Vector vector = (new reconnaissancefacial.ImageProcessing(images.get(idImage), "N&B").toMatrix().toVector());
            Vector vectorProjected = matrixEigenFaces.projectVector(vector);
            faces.put(idImage, vectorProjected);
        }

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
        /* on vas chercher le nom de la personne grace a son id */

        if (distance< Init.EPSILON * 2) {
            String[] name = mysql.getName(closestId);
            closestImage =  images.get(closestId);
            firstNameIdentified = name[0];
            lastNameIdentified = name[1];
        }

        System.out.println("fini");
    }
}
