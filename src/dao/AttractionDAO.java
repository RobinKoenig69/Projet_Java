package dao; 

import model.Attraction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttractionDAO {
    public static List<Attraction> getAllAttractions(Connection connection) throws SQLException {
        List<Attraction> attractions = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Attraction");
        
        while (rs.next()) {
            attractions.add(new Attraction(
                rs.getInt("id_attraction"),
                rs.getString("Nom"),
                rs.getInt("Nb_places_dispo"),
                rs.getFloat("Tarif")
            ));
        }
        return attractions;
    }
}