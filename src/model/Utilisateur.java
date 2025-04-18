package model;

import java.time.LocalDate;

public class Utilisateur {
    public enum Client_type { ANCIEN, NOUVEAU, INVITE }
    public enum Tranche_Age { ENFANT, JEUNE_ADULTE, ADULTE, SENIOR }

    private int id_utilisateur;
    private String nom;
    private String prenom;
    private Client_type clientType;
    private Tranche_Age trancheAge;
    private String email;
    private String adresse;
    private LocalDate dateNaissance;
    private int attractionPrefereeId;

    //constructor
    public Utilisateur(int id_utilisateur, String nom, String prenom,
                       Client_type clientType, Tranche_Age trancheAge,
                       String email, String adresse, LocalDate dateNaissance,
                       int attractionPrefereeId) {

        this.id_utilisateur = id_utilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.clientType = clientType;
        this.trancheAge = trancheAge;
        this.email = email;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.attractionPrefereeId = attractionPrefereeId;
    }

    //getters
    public int getId_utilisateur() { return id_utilisateur; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Client_type getClientType() { return clientType; }
    public Tranche_Age getTrancheAge() { return trancheAge; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public int getAttractionPrefereeId() { return attractionPrefereeId; }
}
