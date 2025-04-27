package Model;

import java.sql.Date;

public class Administrateur extends Utilisateur {
    boolean administrateur;

    public Administrateur(int id_utilisateur, String nom, String prenom, String clientType, String trancheAge, String email, String adresse, Date derniere_visite, int attractionPrefereeId) {
        super(id_utilisateur, nom, prenom, clientType, trancheAge, email, adresse, derniere_visite, attractionPrefereeId);
        this.administrateur = true;
    }

}
