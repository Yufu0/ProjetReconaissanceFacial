package reconnaissancefacial;


import java.io.IOException;

public class Main {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;

    public static final int EPSILON = 3000;

    public static void main(String[] args) throws IOException {
        if (args.length == 1 && args[0].compareTo("init") == 0) {
            final MySQL database =  MySQL.getInstance();
            database.connexion();
            database.clearOldTable();
            InitDataBase.init();
            database.deconnexion();
        }

        if (args.length == 2 && args[0].compareTo("check") == 0) {
            final MySQL database =  MySQL.getInstance();
            database.connexion();
            FaceRecognition faceRecognition = new FaceRecognition(args[1]);
            database.deconnexion();
        }

        if (args.length == 2 && args[0].compareTo("affichage") == 0) {
            final MySQL database =  MySQL.getInstance();
            database.connexion();
            Affichage.affichage(args[1]);
            database.deconnexion();
        }
    }
}