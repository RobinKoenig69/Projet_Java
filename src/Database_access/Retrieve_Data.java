package Database_access;

import java.sql.*;

public class Retrieve_Data {

    public Connection connect() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/projet_java";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connexion réussie !");
            return connection;
        } catch (Exception e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }
}