package Model;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Représente un utilisateur du système.
 * Contient des informations personnelles et préférences de visite.
 */

public class Utilisateur {

    private int id_utilisateur;
    private String nom;
    private String prenom;
    private String clientType;
    private String trancheAge;
    private String email;
    private String adresse;
    private Date derniere_visite;
    private int attractionPrefereeId;

    /**
     * Constructeur utilisateur.
     *
     * @param id_utilisateur       ID utilisateur
     * @param nom                  Nom
     * @param prenom               Prénom
     * @param clientType           Type de client
     * @param trancheAge           Tranche d’âge
     * @param email                Adresse email
     * @param adresse              Adresse postale
     * @param derniere_visite      Dernière visite
     * @param attractionPrefereeId ID attraction préférée
     */

    //constructor
    public Utilisateur(int id_utilisateur, String nom, String prenom,
                       String clientType, String trancheAge,
                       String email, String adresse, Date derniere_visite,
                       int attractionPrefereeId) {

        this.id_utilisateur = id_utilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.clientType = clientType;
        this.trancheAge = trancheAge;
        this.email = email;
        this.adresse = adresse;
        this.derniere_visite = derniere_visite;
        this.attractionPrefereeId = attractionPrefereeId;
    }

    //getters
    public int getId_utilisateur() { return id_utilisateur; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getClientType() { return clientType; }
    public String getTrancheAge() { return trancheAge; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }
    public Date getDerniereVisite() { return derniere_visite; }
    public int getAttractionPrefereeId() { return attractionPrefereeId; }
}