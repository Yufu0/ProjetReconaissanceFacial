package reconnaissancefacial;


import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final int WIDTH = 200;
    public static final int HEIGHT = 300;
    public static final int EPSILON = 10;

    public static void main(String[] args) throws IOException {
        InitDataBase.init();

        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir un le visage :");
        String str = sc.nextLine();

        FaceRecognition faceRecognition = new FaceRecognition(str);
    }

}
