package Model;

import java.sql.Time;
import java.time.LocalDateTime;

public class Attraction {
    private int id;
    private String nom;
    private int Nb_places_dispo;
    private int Nb_places_tot;
    private double tarif;
    private boolean ouvert;
    private String categorie;
    private Time heure_ouverture;
    private Time heure_fermeture;
    private Time heure_fin_inscription;

    //constructor
    public Attraction(int id, String Nom, int Nb_places_dispo,
                      int Nb_places_tot, double tarif,
                      boolean ouvert, String categorie,
                      Time heure_ouverture, Time heure_fermeture,
                      Time heure_fin_inscription) {

        this.id = id;
        this.nom = Nom;
        this.Nb_places_dispo = Nb_places_dispo;
        this.Nb_places_tot = Nb_places_tot;
        this.tarif = tarif;
        this.ouvert = ouvert;
        this.categorie = categorie;
        this.heure_ouverture = heure_ouverture;
        this.heure_fermeture = heure_fermeture;
        this.heure_fin_inscription = heure_fin_inscription;
    }

    //getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getPlacesDispo() { return Nb_places_dispo; }
    public double getTarif() { return tarif; }
    public boolean isOuvert() { return ouvert; }
    public String getCategorie() { return categorie; }
    public Time getHeureOuverture() { return heure_ouverture; }
    public Time getHeureFermeture() { return heure_fermeture; }
    public Time getHeureFinInscription() { return heure_fin_inscription; }
    public int getNb_places_dispo() { return Nb_places_dispo; }
    public int getNb_places_tot() { return Nb_places_tot; }
}