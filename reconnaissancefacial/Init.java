package reconnaissancefacial;


import java.io.File;
import java.io.IOException;

public class Init {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;
    public static final int EPSILON = 3000;
    public static final int NUMBER_EIGENFACES = 5;

    public void initialiser() throws IOException {
        //Suppression du fichier DataBaseImage
        File dossier = new File("./img/DataBaseImage");
        if(deleteDirectory(dossier)){
            System.out.println(dossier.getName() + " est supprimé.");
        }else{
            System.out.println("Opération de suppression echouée");
        }

        //Suppression des tables existantes
        MySQL.getInstance().clearOldTable();

        //Initilisation des nouvelles tables
        InitDataBase.init();
    }
/*
    public static void main(String[] args) throws IOException {

        //Suppression du fichier DataBaseImage
        File dossier = new File("./img/DataBaseImage");
        if(deleteDirectory(dossier)){
            System.out.println(dossier.getName() + " est supprimé.");
        }else{
            System.out.println("Opération de suppression echouée");
        }

        //Connexion à la BDD
        MySQL database =  MySQL.getInstance();
        database.connexion();

        //Suppression des tables existantes
        database.clearOldTable();

        //Initilisation des nouvelles tables
        InitDataBase.init();

        database.deconnexion();
    }
*/
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}