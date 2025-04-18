package Model;

import java.time.LocalDateTime;

public class Attraction {
    private int id_attraction;
    private String nom;
    private int Nb_places_dispo;
    private int Nb_places_tot;
    private float tarif;
    private boolean ouvert;
    private String categorie;
    private LocalDateTime heure_ouverture;
    private LocalDateTime heure_fermeture;
    private LocalDateTime heure_fin_inscription;

    //constructor
    public Attraction(int Id_attraction, String Nom, int Nb_places_dispo,
                      int Nb_places_tot, float tarif,
                      boolean ouvert, String categorie,
                      LocalDateTime heure_ouverture, LocalDateTime heure_fermeture,
                      LocalDateTime heure_fin_inscription) {

        this.id_attraction = Id_attraction;
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
    public String getNom() { return nom; }
    public int getPlacesDispo() { return Nb_places_dispo; }
    public float getTarif() { return tarif; }
    public boolean isOuvert() { return ouvert; }
    public String getCategorie() { return categorie; }
    public LocalDateTime getHeureOuverture() { return heure_ouverture; }
    public LocalDateTime getHeureFermeture() { return heure_fermeture; }
    public LocalDateTime getHeureFinInscription() { return heure_fin_inscription; }
    public int getId_attraction() { return id_attraction; }
    public int getNb_places_dispo() { return Nb_places_dispo; }
    public int getNb_places_tot() { return Nb_places_tot; }
}