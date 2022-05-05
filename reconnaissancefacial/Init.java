package reconnaissancefacial;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Init {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;
    public static final int EPSILON = 3000;
    public static final int NUMBER_EIGENFACES = 5;

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {

        File dossier = new File("./img/DataBaseImage");
        if(deleteDirectory(dossier)){
            System.out.println(dossier.getName() + " est supprimé.");
        }else{
            System.out.println("Opération de suppression echouée");
        }

        MySQL database =  MySQL.getInstance();
        database.connexion();
        database.clearOldTable();
        InitDataBase.init();
        database.deconnexion();
    }

    static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}