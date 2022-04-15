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

        /* calcule de l'ACP sur cette matrice */
        ACP acp = new ACP(matrixImages);
        System.out.println("Projection sur  : " + acp.getEigenMatrix().getWidth() + " eigenfaces");

        /* projection du vecteur de l'image sur l'espace des eigenfaces */
        Vector vectorToRecognizeProjected = acp.getEigenMatrix().projectVector(vectorToRecognize);

        /* on projettent tous les vecteurs sur l'espace des eigenfaces */
        HashMap<Integer, Vector> faces = new HashMap<>();
        for (int idImage : images.keySet()) {
            Vector vector = (new ImageProcessing(images.get(idImage), "N&B").toMatrix().toVector());
            Vector vectorProjected = acp.getEigenMatrix().projectVector(vector);
            faces.put(idImage, vectorProjected);
        }
        System.out.println(vectorToRecognizeProjected.getLenght());

        /* comparaison avec tous les visages pour determiner le plus proche */
        int closestId = -1;
        double distance = -1;
        double total =0.0;
        for (int id : faces.keySet()) {
            double newDistance = vectorToRecognizeProjected.compareTo(faces.get(id));
            total += newDistance;
            System.out.println(newDistance);
            if (distance < 0 || distance > newDistance) {
                distance = newDistance;
                closestId = id;
            }
        }
        System.out.println("precision : " + (int)(100*distance/total*faces.size()) + " %");


        /* affichage du resultat */
        /* on vas chercher le nom de la personne grace a son id */
        String name = mysql.getName(closestId);
        if (distance/total*faces.size() < 0.1) {
            System.out.println("C'est le visage de " + name + " (" + (int) (100*distance/total*faces.size()) + "%)");
        } else if ((distance/total*faces.size()) < 0.2) {
            System.out.println("C'est surement le visage de " + name + " (" + (int) (100*distance/total*faces.size())+ "%)");
        } else  if (distance/total*faces.size() < 0.3) {
            System.out.println("C'est peut-être le visage de " + name + " (" + (int) (100*distance/total*faces.size())+ "%)");
        } else {
            System.out.println("Le visage est inconnu (" + (int) (100*distance/total*faces.size())+ "%)");
        }
    }
}
