package reconnaissancefacial;

import java.io.File;
import java.io.IOException;

public class InitDataBase {
    public static void init() throws IOException {
        
        String src = "img/Base_Images_Apprentissage";
        File file = new File(src);

        String nom;
        String prenom;

        for(String str : file.list()) {
            nom = str.split("_")[1];
            prenom = str.split("_")[0];
            File file2 = new File(src + "/" +str);
            for(String image : file2.list()) {
                InitDataBase.addImageToDataBase(nom, prenom, src + "/" + str + "/"+image);
            }
        }
    }


    public static void addImageToDataBase(String nom, String prenom, String source) throws IOException {

        String name = new File(source).getName();
        ImageProcessing img = new ImageProcessing(source, "COULEUR");
        img.toBlackAndWhite();
        img.resize(Main.WIDTH,Main.HEIGHT);
        img.save("img/DataBaseImage/" + name);

        MySQL mysql = MySQL.getInstance();
        mysql.addImage(nom, prenom, source);
    }
}
