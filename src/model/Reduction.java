package model;

import java.time.LocalDateTime;

public class Reduction {

    public enum ConcerneType { ANCIEN, NOUVEAU }

    private int idReduction;
    private int pourcentage; // entre 0 et 100
    private ConcerneType concerne;
    private int idAttraction;
    private LocalDateTime dateReservation; // si tu veux inclure une date

    // Constructeur
    public Reduction(int idReduction, int pourcentage, ConcerneType concerne, int idAttraction, LocalDateTime dateReservation) {
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100.");
        }
        this.idReduction = idReduction;
        this.pourcentage = pourcentage;
        this.concerne = concerne;
        this.idAttraction = idAttraction;
        this.dateReservation = dateReservation;
    }

    // Getters
    public int getIdReduction() {return idReduction;}
    public int getPourcentage() {return pourcentage;}
    public ConcerneType getConcerne() {return concerne;}
    public int getIdAttraction() { return idAttraction;}
    public LocalDateTime getDateReservation() { return dateReservation;}
}
