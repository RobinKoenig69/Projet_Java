package Model;

import java.time.LocalDateTime;

public class Reduction {



    private int idReduction;
    private int pourcentage; // entre 0 et 100
    private String concerne;
    private int idAttraction;
    private LocalDateTime dateReservation; // si tu veux inclure une date

    // Constructeur
    public Reduction(int idReduction, int pourcentage, String concerne, int idAttraction) {
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100.");
        }
        this.idReduction = idReduction;
        this.pourcentage = pourcentage;
        this.concerne = concerne;
        this.idAttraction = idAttraction;
    }

    // Getters
    public int getIdReduction() {return idReduction;}
    public int getPourcentage() {return pourcentage;}
    public String getConcerne() {return concerne;}
    public int getIdAttraction() { return idAttraction;}
}
