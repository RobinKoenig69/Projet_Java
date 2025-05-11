package Model;

import java.time.LocalDateTime;

/**
 * Représente une réduction applicable à une attraction spécifique.
 * Peut concerner un type de client ou une promotion ciblée.
 */

public class Reduction {

    private int idReduction;
    private int pourcentage; // entre 0 et 100
    private String concerne;
    private int idAttraction;
    private LocalDateTime dateReservation; // si tu veux inclure une date

    /**
     * Constructeur de réduction.
     *
     * @param idReduction  ID de la réduction
     * @param pourcentage  Pourcentage de réduction (0 à 100)
     * @param concerne      Public concerné par la réduction
     * @param idAttraction ID de l’attraction concernée
     * @throws IllegalArgumentException si le pourcentage est hors de [0,100]
     */

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
