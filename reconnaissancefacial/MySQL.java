package reconnaissancefacial;

import java.sql.*;
import java.util.HashMap;

public class MySQL {

    private static final MySQL instance = new MySQL("jdbc:mysql://","localhost","rf","root","");

    private final String urlBase;
    private final String host;
    private final String database;
    private final String userName;
    private final String password;
    private Connection connection;

    public MySQL(String urlBase, String host, String database, String userName, String password) {
        this.urlBase = urlBase;
        this.host = host;
        this.database = database;
        this.userName = userName;
        this.password = password;
    }

    /* Connexion MySQL + création tables */
    public void connexion() {
        if (!isOnline()) {
            try {
                connection = DriverManager.getConnection(this.urlBase + this.host  + "/" + this.database, this.userName, this.password);
                Statement preparedStatement = connection.createStatement();
                String createTablePersonne = "CREATE TABLE IF NOT EXISTS personne (" +
                        "idPersonne INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "nom varchar(255) not NULL," +
                        "prenom varchar(255) not NULL);";
                preparedStatement.executeUpdate(createTablePersonne);

                String createTableImage = "CREATE TABLE IF NOT EXISTS image (" +
                        "idImage INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "src varchar(255) not NULL," +
                        "idPersonne INTEGER not NULL," +
                        "CONSTRAINT fk_personne FOREIGN KEY (idPersonne) REFERENCES personne(idPersonne));";
                preparedStatement.executeUpdate(createTableImage);

                String createTableEigenface = "CREATE TABLE IF NOT EXISTS eigenface (" +
                        "idEigenface INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "valPropre DOUBLE);";
                preparedStatement.executeUpdate(createTableEigenface);

                String createTableValeur = "CREATE TABLE IF NOT EXISTS valeur (" +
                        "idValeur INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "index_ DOUBLE not NULL, " +
                        "idEigenface INTEGER not NULL," +
                        "CONSTRAINT fk_eigenface FOREIGN KEY (idEigenface) REFERENCES eigenface(idEigenface));";
                preparedStatement.executeUpdate(createTableValeur);

                String createTableProjection = "CREATE TABLE IF NOT EXISTS projection (" +
                        "idProjection INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "idImage INTEGER not NULL," +
                        "CONSTRAINT fk_image FOREIGN KEY (idImage) REFERENCES image(idImage));";
                preparedStatement.executeUpdate(createTableProjection);

                String createTableValeur2 = "CREATE TABLE IF NOT EXISTS valeur2 (" +
                        "idValeur INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "index_ DOUBLE not NULL, " +
                        "idProjection INTEGER not NULL," +
                        "CONSTRAINT fk_projection FOREIGN KEY (idProjection) REFERENCES projection(idProjection));";
                preparedStatement.executeUpdate(createTableValeur2);

                preparedStatement.close();
                System.out.println("[MySQL] Connexion effectuée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /* Récupérer la connexion de la BDD */
    public Connection getConnexion() {
        return connection;
    }

    /* Récupérer l'instance de la BDD */
    public static MySQL getInstance() {
        return instance;
    }

    /* Deconnection de MySQL */
    public void deconnexion() {
        if (isOnline()) {
            try {
                connection.close();
                System.out.println("[MySQL] Déconnexion effectutée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /* Permet de savoir si déjà co */
    public boolean isOnline() {
        try {
            return (connection != null) && (!connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Matrix getEigenfaces() {
        try {
            int current;
            int i;
            int j;

            Matrix m;

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM eigenface");
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) m = new Matrix(rs.getInt(1), Main.WIDTH * Main.HEIGHT);
            else throw new SQLException("Erreur récupération nombre d'eigenfaces");
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("SELECT index_,idEigenface FROM valeur ORDER BY idEigenface;");
            rs = preparedStatement.executeQuery();
            if(rs.next()) {
                i=0;
                j=0;
                current = rs.getInt(2);
                Vector v = new Vector(Main.WIDTH * Main.HEIGHT);
                v.set(i,rs.getDouble(1));
                while(rs.next()) {
                    i++;
                    if (current==rs.getInt(2)) {
                        v.set(i, rs.getDouble(1));
                    }
                    else {
                        m.getVectors()[j] = v;
                        v = new Vector(Main.WIDTH * Main.HEIGHT);
                        i = -1;
                        j++;
                        current=rs.getInt(2);
                    }
                }
                m.getVectors()[j] = v;
                preparedStatement.close();
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, Vector> getProjectedFaces() {
        try {
            Integer current = null;
            int i = 0;
            int lengt;
            HashMap<Integer, Vector> map = new HashMap<>();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM valeur2 GROUP BY idProjection LIMIT 1;");
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) lengt = rs.getInt(1);
            else throw new SQLException("Erreur récupération nombre d'eigenfaces");
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("SELECT index_,idProjection FROM valeur2 ORDER BY idProjection;");
            rs = preparedStatement.executeQuery();

            Vector v = new Vector(lengt);
            while(rs.next()){
                if(current == null) current=rs.getInt(2);
                if (current==rs.getInt(2)) {
                    v.set(i, rs.getDouble(1));
                    i++;
                } else {
                    map.put(current, v);
                    v = new Vector(lengt);
                    i = 0;
                    current=rs.getInt(2);
                }
            }
            preparedStatement.close();
            map.put(current, v);
            return map;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName(int idImage) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT nom, prenom from personne,image WHERE personne.idPersonne = image.idPersonne AND image.idImage = ?");
            preparedStatement.setInt(1, idImage);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) return "" + rs.getString(1) + " " + rs.getString(2);
            else throw new SQLException("Erreur récupération nom et prénom");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addEigenface(Vector eigenface, double valPropre) {
        try {

            int index;
            System.out.println("Le programme est pas mort");
            // Création de l'eigenface
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO eigenface (valPropre) VALUES (?)");
            preparedStatement.setDouble(1, valPropre);
            preparedStatement.execute();
            preparedStatement.close();


            preparedStatement= connection.prepareStatement("SELECT LAST_INSERT_ID() LIMIT 1");
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) index = rs.getInt(1);
            else throw new SQLException("Erreur récupération index auto incrément");
            preparedStatement.close();


            // Ajout des valeurs avec comme clef étrangère l'auto incrément précédement récupéré
            for (double val: eigenface.getData()) {
                preparedStatement = connection.prepareStatement("INSERT INTO valeur (index_,idEigenface) VALUES (?,?)");
                preparedStatement.setDouble(1, val);
                preparedStatement.setInt(2, index);
                preparedStatement.execute();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPersonne(String nom, String prenom) {
        // Ajout d'un personne
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO personne (nom,prenom) VALUES (?,?)");
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addImage(String nom, String prenom, String src) {
        // Récupération de l'id de la personne
        int idPersonne = existPerson(nom, prenom);

        // Si la personne n'existe pas on la créée
        if( idPersonne == 0) {
            addPersonne(nom,prenom);
            idPersonne = existPerson(nom, prenom);
        }

        // Ajout de l'image
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO image (src, idPersonne) VALUES (?,?)");
            preparedStatement.setString(1, src);
            preparedStatement.setInt(2, idPersonne);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int existPerson(String nom, String prenom) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idPersonne FROM personne WHERE nom = ? AND prenom = ?");
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) return rs.getInt(1);
            else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addProjectedFace(Vector projection, int id) {
        try {
            int index;
            // Création de la projection
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO projection (idImage) VALUES (?)");
            preparedStatement.setDouble(1, id);
            preparedStatement.execute();
            preparedStatement.close();

            preparedStatement= connection.prepareStatement("SELECT LAST_INSERT_ID() LIMIT 1");
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) index = rs.getInt(1);
            else throw new SQLException("Erreur récupération index auto incrément");
            preparedStatement.close();

            // Ajout des valeurs avec comme clef étrangère l'auto incrément précédement récupéré
            for (double val: projection.getData()) {
                preparedStatement = connection.prepareStatement("INSERT INTO valeur2 (index_,idProjection) VALUES (?,?)");
                preparedStatement.setDouble(1, val);
                preparedStatement.setInt(2, index);
                preparedStatement.execute();
                preparedStatement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, String> getImages() {
        HashMap<Integer, String> map = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idImage,src FROM image;");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt(1),rs.getString(2));
            }
            preparedStatement.close();
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
