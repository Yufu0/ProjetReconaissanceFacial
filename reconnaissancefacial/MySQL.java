package reconnaissancefacial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
                Statement s = connection.createStatement();
                String createTablePersonne = "CREATE TABLE IF NOT EXISTS personne (idPersonne INTEGER , nom varchar(255), prenom varchar(255));";
                s.executeUpdate(createTablePersonne);
                String createTableImage = "CREATE TABLE IF NOT EXISTS image (idImage INTEGER , src varchar(255));";
                s.executeUpdate(createTableImage);
                String createTableEigenface = "CREATE TABLE IF NOT EXISTS eigenface (idEigenface INTEGER , valPropre DOUBLE);";
                s.executeUpdate(createTableEigenface);
                String createTableValeur = "CREATE TABLE IF NOT EXISTS valeur (idValeur INTEGER , index_ INTEGER);";
                s.executeUpdate(createTableValeur);
                String createTableProjeter = "CREATE TABLE IF NOT EXISTS projeter (idProjeter INTEGER);";
                s.executeUpdate(createTableProjeter);
                s.close();
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
        return null;
    }

    public HashMap<Integer, Vector> getProjectedFaces() {
        return null;
    }

    public String getName(int id) {
        return null;
    }
}
