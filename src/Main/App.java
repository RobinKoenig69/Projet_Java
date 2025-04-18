
public class App {
    public static void main(String[] args) {
        Retrieve_Data rd = new Retrieve_Data();
        rd.getData();
    }
}


class Retrieve_Data {
    public void getData() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/projet_java";
        String username = "root";
        String password = "";

        try {
            // Connexion à la base
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connexion réussie !");

            // recup liste des attractions
            List<Attraction> attractions = AttractionDAO.getAllAttractions(connection);

            // afficher les attractions
            System.out.println("\n--- LISTE DES ATTRACTIONS ---");
            for (Attraction attraction : attractions) {
                System.out.println(
                    attraction.getNom() + " | " +
                    "Places dispo: " + attraction.getPlacesDispo() + " | " +
                    "Prix: " + attraction.getTarif() + "€"
                );
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}