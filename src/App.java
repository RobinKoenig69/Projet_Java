import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/projet_java";
        String username = "root";
        String password = ""; // Laisser vide si c’est le cas par défaut avec XAMPP

        try {
            // Connexion à la base
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connexion réussie !");


        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}