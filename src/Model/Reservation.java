package Model;

import java.time.LocalDateTime;

/**
 * Représente une réservation effectuée par un utilisateur.
 * Contient les informations de date, de prix et d'annulation.
 */

public class Reservation {
    private int id_reservation;
    private LocalDateTime Date_reservation;
    private float Prix;
    private int id_utilisateur;
    private int id_attraction;
    private boolean estAnnulee;

    /**
     * Constructeur de réservation.
     *
     * @param id_reservation ID de la réservation
     * @param DATETIME       Date et heure de la réservation
     * @param Prix           Prix payé
     * @param id_utilisateur ID de l'utilisateur
     * @param id_attraction  ID de l’attraction réservée
     */

    // Constructor
    public Reservation(int id_reservation, LocalDateTime DATETIME, float Prix, 
                      int id_utilisateur, int id_attraction) {
        this.id_reservation = id_reservation;
        this.Date_reservation = DATETIME;
        this.Prix = Prix;
        this.id_utilisateur = id_utilisateur;
        this.id_attraction = id_attraction;
        this.estAnnulee = false;
    }

   
    // Getters
    public int getId_reservation() { return id_reservation; }
    public LocalDateTime getDate_reservation() { return Date_reservation; }
    public float getPrix() { return Prix; }
    public int getId_utilisateur() { return id_utilisateur; }
    public int getId_attraction() { return id_attraction; }
    public boolean isEstAnnulee() { return estAnnulee; }

    // Setters
    public void setEstAnnulee(boolean estAnnulee) { 
        this.estAnnulee = estAnnulee; 
    }

    /**
     * Calcule et applique une réduction au prix total.
     *
     * @param tauxReduction Taux de réduction entre 0 et 1 (ex: 0.2 pour 20%)
     */

    // Méthode métier
    public void calculerPrixAvecReduction(float tauxReduction) {
        if (tauxReduction > 0 && tauxReduction <= 1) {
            this.Prix -= this.Prix * tauxReduction;
        }
    }

    @Override
    public String toString() {
        return String.format("Réservation #%d - Attraction: %d - User: %d - Prix: %.2f€", 
                           id_reservation, id_attraction, id_utilisateur, Prix);
    }
}