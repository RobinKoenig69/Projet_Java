package Model;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * Classe utilitaire de session pour stocker les données utilisateur courantes.
 *
 * Toutes les données sont statiques : cette classe joue le rôle de singleton implicite.
 */

public class Session {
    private static String userName;
    private static int userID;
    private static int userBooking;
    private static boolean admin;
    private static float cartvalue;
    private static float nbpersonnes;
    private static String categorie;
    private static Date datebooking;

    /**
     * Initialise une session utilisateur.
     *
     * @param userName     Nom d’utilisateur
     * @param userID       ID utilisateur
     * @param userBooking  Nombre de réservations en cours
     * @param admin        Statut administrateur
     * @param categorie    Catégorie de client
     */

    public Session(String userName, int userID, int  userBooking, boolean admin, String categorie) {
        Session.userName = userName;
        Session.userID = userID;
        Session.userBooking = userBooking;
        Session.admin = admin;
        Session.cartvalue = 0;
        Session.categorie = categorie;
        Session.datebooking = null;
        Session.nbpersonnes = 0;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String name) {
        userName = name;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int id) {
        userID = id;
    }

    public static int getUserBooking() { return userBooking; }

    public static void setUserBooking(int  booking) { userBooking = booking; }

    public static boolean getAdmin() { return admin; }

    public static void setAdmin(boolean admin) { admin = admin; }

    public static float getCartvalue() { return cartvalue; }

    public static void setCartvalue(float value) { cartvalue = value; }

    public static String getCategorie() { return categorie; }

    public static void setCategorie(String value) { categorie = value; }

    public static Date getDatebooking() { return datebooking; }

    public static void setDatebooking(Date value) { datebooking = value; }

    public static float getNbpersonnes() { return nbpersonnes; }

    public static void setNbpersonnes(float value) { nbpersonnes = value; }
}
