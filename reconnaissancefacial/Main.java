package reconnaissancefacial;


import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final int EPSILON = WIDTH*HEIGHT/100;

    public static void main(String[] args) throws IOException {

        final MySQL database =  MySQL.getInstance();
        database.connexion();


        //InitDataBase.init();

        System.out.println("fin");

        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir un le visage :");
        String str = sc.nextLine();

        FaceRecognition faceRecognition = new FaceRecognition(str);

        database.deconnexion();
    }

}
/*img/Base_Images_Apprentissage/CELIO_BUERI/CELIO_BUERI_0.jpg*/