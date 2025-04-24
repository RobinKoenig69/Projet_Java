package model;

public class Reduction {
    private int id;
private int pourcentage;
private String concerne;
private int idAttraction;

public Reduction(int id, int pourcentage, String concerne, int idAttraction) {
this.id = id;
this.pourcentage = pourcentage;
this.concerne = concerne;
this.idAttraction = idAttraction;
}

@Override
public String toString() {
return "Réduction " + pourcentage + "% pour : " + concerne + ", Attraction ID : " + idAttraction;
}
}


