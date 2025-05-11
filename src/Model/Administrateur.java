package Model;

import java.sql.Date;

/**
 * Représente un utilisateur ayant un statut d'administrateur.
 * Hérite de la classe {@link Utilisateur}.
 *
 * Cette classe permet d'étendre les droits d’un utilisateur standard
 * en spécifiant qu’il s’agit d’un administrateur du système.
 */

public class Administrateur extends Utilisateur {
    boolean administrateur;

    /**
     * Constructeur d’un administrateur.
     *
     * @param id_utilisateur       ID de l'utilisateur
     * @param nom                  Nom de l'utilisateur
     * @param prenom               Prénom de l'utilisateur
     * @param clientType           Type de client
     * @param trancheAge           Tranche d'âge
     * @param email                Adresse email
     * @param adresse              Adresse postale
     * @param derniere_visite      Date de dernière visite
     * @param attractionPrefereeId ID de l’attraction préférée
     */

    public Administrateur(int id_utilisateur, String nom, String prenom, String clientType, String trancheAge, String email, String adresse, Date derniere_visite, int attractionPrefereeId) {
        super(id_utilisateur, nom, prenom, clientType, trancheAge, email, adresse, derniere_visite, attractionPrefereeId);
        this.administrateur = true;
    }

}
