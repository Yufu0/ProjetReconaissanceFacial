package reconnaissancefacial;


import java.io.IOException;

public class Main {
    public static final int WIDTH = 200;
    public static final int HEIGHT = 300;
    public static final int EPSILON = 10000;

    public static void main(String[] args) throws IOException {
        if (args.length == 1 && args[0].compareTo("init") == 0) {
            final MySQL database =  MySQL.getInstance();
            database.connexion();
            InitDataBase.init();
            database.deconnexion();
        }

        if (args.length == 2 && args[0].compareTo("check") == 0) {
            final MySQL database =  MySQL.getInstance();
            database.connexion();
            FaceRecognition faceRecognition = new FaceRecognition(args[1]);
            database.deconnexion();
        }
    }

}
/*img/Base_Images_Apprentissage/CELIO_BUERI/CELIO_BUERI_0.jpg*/