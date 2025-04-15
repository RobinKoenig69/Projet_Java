public class Attraction {
    private int id;
    private String nom;
    private int placesDispo;
    private float tarif;

    //constructeur
    public Attraction(int id, String nom, int placesDispo, float tarif) {
        this.id = id;
        this.nom = nom;
        this.placesDispo = placesDispo;
        this.tarif = tarif;
    }

    //getters
    public String getNom() { return nom; }
    public int getPlacesDispo() { return placesDispo; }
    public float getTarif() { return tarif; }
}