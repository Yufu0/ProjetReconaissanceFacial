package reconnaissancefacial;

import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class MySQL {

    private static final MySQL instance = new MySQL("jdbc:mysql://","localhost","Reconnaissance","root","");

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
                Connection connection1 = DriverManager.getConnection(this.urlBase + this.host , this.userName, this.password);
                Statement preparedStatement1 = connection1.createStatement();
                preparedStatement1.executeUpdate("CREATE DATABASE IF NOT EXISTS "+this.database+";"); // now the database physically exists
                preparedStatement1.close();
                connection = DriverManager.getConnection(this.urlBase + this.host  + "/" + this.database , this.userName, this.password);
                Statement preparedStatement = connection.createStatement();
                String createTablePersonne = "CREATE TABLE IF NOT EXISTS Personne (" +
                        "idPersonne INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "nom varchar(255) not NULL," +
                        "prenom varchar(255) not NULL);";
                preparedStatement.executeUpdate(createTablePersonne);

                String createTableImage = "CREATE TABLE IF NOT EXISTS Image (" +
                        "idImage INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "src varchar(255) not NULL," +
                        "idPersonne INTEGER not NULL," +
                        "CONSTRAINT fk_personne FOREIGN KEY (idPersonne) REFERENCES Personne(idPersonne));";
                preparedStatement.executeUpdate(createTableImage);


                String createTableEigenFaces = "CREATE TABLE IF NOT EXISTS EigenFaces (" +
                        "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                        "matrix LONGBLOB not NULL);";
                preparedStatement.executeUpdate(createTableEigenFaces);

                preparedStatement.close();
                System.out.println("[MySQL] Connexion effectuée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearOldTable(){
        if (isOnline()) {
            try {
                Statement preparedStatement = connection.createStatement();
                String deleteTableImage = "DELETE FROM Image";
                preparedStatement.executeUpdate(deleteTableImage);

                String deleteTablePersonne = "DELETE FROM Personne";
                preparedStatement.executeUpdate(deleteTablePersonne);

                String deleteTableEigenFaces = "DELETE FROM EigenFaces";
                preparedStatement.executeUpdate(deleteTableEigenFaces);

                preparedStatement.close();
                System.out.println("[MySQL] Suppression effectuée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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



    public String[] getName(int idImage) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT nom, prenom from Personne,Image WHERE Personne.idPersonne = Image.idPersonne AND Image.idImage = ?");
            preparedStatement.setInt(1, idImage);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) return new String[]{rs.getString(1), rs.getString(2)};
            else throw new SQLException("Erreur récupération nom et prénom");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void addPersonne(String nom, String prenom) {
        // Ajout d'un Personne
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Personne (nom,prenom) VALUES (?,?)");
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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Image (src, idPersonne) VALUES (?,?)");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idPersonne FROM Personne WHERE nom = ? AND prenom = ?");
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



    public HashMap<Integer, String> getImages() {
        HashMap<Integer, String> map = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idImage,src FROM Image;");
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

    public void saveEigenFaces(Matrix matrix) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM EigenFaces;");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO EigenFaces (matrix) VALUES (?)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(baos);
            oout.writeObject(matrix);
            oout.close();
            preparedStatement.setBytes(1, baos.toByteArray());

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Matrix getEigenFaces() throws SQLException, IOException, ClassNotFoundException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT matrix from EigenFaces");
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            byte[] buf = rs.getBytes(1);
            if (buf != null) {
                ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
                return (Matrix) objectIn.readObject();
            }
        }
        preparedStatement.close();
        return null;
    }
}