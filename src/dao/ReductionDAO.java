package dao;

import model.Reduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReductionDAO {
    public static List<Reduction> getAllReductions(Connection conn) throws SQLException {
        List<Reduction> reductions = new ArrayList<>();
        String sql = "SELECT * FROM Reduction";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reduction red = new Reduction(
                        rs.getInt("id_reduction"),
                        rs.getInt("Pourcentage"),
                        rs.getString("Concerne"),
                        rs.getInt("id_attraction")
                );
                reductions.add(red);
            }
        }

        return reductions;
    }
}
