package reconnaissancefacial;


import java.io.IOException;
import java.sql.SQLException;

public class Init {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;

    public static final int EPSILON = 3000;
    
    public static final int NUMBER_EIGENFACES = 5;

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        MySQL database =  MySQL.getInstance();
        database.connexion();
        database.clearOldTable();
        InitDataBase.init();
        database.deconnexion();
    }
}
