package Model;

import java.time.LocalDateTime;

public class Reservation {
    private int id_reservation;
    private LocalDateTime Date_reservation;
    private float Prix;
    private int id_utilisateur;
    private int id_attraction;

    //constructor
    public Reservation(int id_reservation, LocalDateTime DATETIME, float Prix, int id_utilisateur, int id_attraction) {
        this.id_reservation = id_reservation;
        this.Date_reservation = DATETIME;
        this.Prix = Prix;
        this.id_utilisateur = id_utilisateur;
        this.id_attraction = id_attraction;
    }

    //getters
    public int getId_reservation() {return id_reservation;}
    public LocalDateTime getDate_reservation() {return Date_reservation;}
    public float getPrix() {return Prix;}
    public int getId_utilisateur() {return id_utilisateur;}
    public int getId_attraction() {return id_attraction;}
}
