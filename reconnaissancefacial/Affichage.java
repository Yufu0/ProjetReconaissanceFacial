package reconnaissancefacial;

import java.io.IOException;
import java.util.HashMap;

public class Affichage {
    public static void affichage(String imageSrc) throws IOException {
        /* ouverture de l'image et modification au format souhaité */

        MySQL mysql = MySQL.getInstance();

        ImageProcessing faceToRecognize = new ImageProcessing(imageSrc, "COULEUR");
        faceToRecognize.toMatrix().export("ImageEntre.jpg");
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
        ACP acp = new ACP(matrixImages, 1);


        acp.getVectorMean().toMatrix(Main.WIDTH).export("visageMoyen.jpg");
        vectorToRecognize=vectorToRecognize.toMatrix(Main.WIDTH).add(acp.getVectorMean().toMatrix(Main.WIDTH).multiplyByConstant(-1)).toVector();
        /* projection du vecteur de l'image sur l'espace des eigenfaces */
        Vector vectorToRecognizeProjected = acp.getEigenMatrix().projectVector(vectorToRecognize);

        Matrix imageReconstruite = new Matrix(Main.WIDTH, Main.HEIGHT);
        imageReconstruite=imageReconstruite.add(acp.getVectorMean().toMatrix(Main.WIDTH));
        imageReconstruite.export("imageReconstruit.jpg");
        double eeeee=0;
        for (int k = 0; k < acp.getEigenVectors().size(); k++) {
            imageReconstruite=imageReconstruite.add(acp.getEigenMatrix().getVectors()[acp.getEigenVectors().size()-k-1].toMatrix(Main.WIDTH).multiplyByConstant(vectorToRecognizeProjected.get(acp.getEigenVectors().size()-k-1)));
            System.out.println("k = "+k+" : erreur = " + (eeeee-imageReconstruite.compareTo(faceToRecognize.toMatrix())));
            System.out.println(eeeee=imageReconstruite.compareTo(faceToRecognize.toMatrix()));
            imageReconstruite.export("imageReconstruit"+k+".jpg");
        }

        for (int k = 0; k < acp.getEigenVectors().size(); k++) {
            acp.getEigenMatrix().getVectors()[k].toMatrix(Main.WIDTH).multiplyByConstant(20000).export("Eigenfaces"+k+".jpg");
        }
///*
//        /* on projettent tous les vecteurs sur l'espace des eigenfaces */
//        HashMap<Integer, Vector> faces = new HashMap<>();
//        for (int idImage : images.keySet()) {
//            Vector vector = (new ImageProcessing(images.get(idImage), "N&B").toMatrix().toVector());
//            Vector vectorProjected = acp.getEigenMatrix().projectVector(vector);
//            faces.put(idImage, vectorProjected);
//        }
//        System.out.println(vectorToRecognizeProjected.getLenght());
//
//        /* comparaison avec tous les visages pour determiner le plus proche */
//        int closestId = -1;
//        double distance = -1;
//        double total =0.0;
//        for (int id : faces.keySet()) {
//            double newDistance = vectorToRecognizeProjected.compareTo(faces.get(id));
//            total += newDistance;
//            System.out.println(newDistance);
//            if (distance < 0 || distance > newDistance) {
//                distance = newDistance;
//                closestId = id;
//            }
//        }
//        System.out.println("precision : " + (int)(100*distance/total*faces.size()) + " %");
//
//
//        /* affichage du resultat */
//        /* on vas chercher le nom de la personne grace a son id */
//        String name = mysql.getName(closestId);
//        if (distance/total*faces.size() < 0.1) {
//            System.out.println("C'est le visage de " + name + " (" + (int) (100*distance/total*faces.size()) + "%)");
//        } else if ((distance/total*faces.size()) < 0.2) {
//            System.out.println("C'est surement le visage de " + name + " (" + (int) (100*distance/total*faces.size())+ "%)");
//        } else  if (distance/total*faces.size() < 0.3) {
//            System.out.println("C'est peut-être le visage de " + name + " (" + (int) (100*distance/total*faces.size())+ "%)");
//        } else {
//            System.out.println("Le visage est inconnu (" + (int) (100*distance/total*faces.size())+ "%)");
//        }*/
    }
}