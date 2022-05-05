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
                addImageToDataBase(nom, prenom, src + "/" + str + "/"+image);
            }
        }

        ImageDatabaseComputation.compute();
    }


    public static void addImageToDataBase(String nom, String prenom, String source) throws IOException {
        reconnaissancefacial.MySQL mysql = reconnaissancefacial.MySQL.getInstance();

        String dirImages = "img/DataBaseImage";
        File dirFileImages = new File(dirImages);
        if (! dirFileImages.exists()) dirFileImages.mkdir();

        System.out.println(source);

        String name = new File(source).getName();
        ImageProcessing img = new ImageProcessing(source, "COULEUR");

        String dir = "img/DataBaseImage/" + prenom.toUpperCase() + "_" + nom.toUpperCase();
        File dirFile = new File(dir);
        if (! dirFile.exists()) dirFile.mkdir();

        String file = dir + "/" + name;
        img.save(file);
        mysql.addImage(nom, prenom, file);
    }
}
