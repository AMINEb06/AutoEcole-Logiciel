package autoécole.model;

public class Seance {
    private int idSeance;
    private String date;
    private String heure;
    private int idEleve;
    private int idMoniteur;
    private int idVehicule;

    public Seance() {}

    public Seance(int idSeance, String date, String heure, int idEleve, int idMoniteur, int idVehicule) {
        this.idSeance = idSeance;
        this.date = date;
        this.heure = heure;
        this.idEleve = idEleve;
        this.idMoniteur = idMoniteur;
        this.idVehicule = idVehicule;
    }

    public Seance(String date, String heure, int idEleve, int idMoniteur, int idVehicule) {
        this.date = date;
        this.heure = heure;
        this.idEleve = idEleve;
        this.idMoniteur = idMoniteur;
        this.idVehicule = idVehicule;
    }

    public int getIdSeance() { return idSeance; }
    public void setIdSeance(int idSeance) { this.idSeance = idSeance; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }

    public int getIdEleve() { return idEleve; }
    public void setIdEleve(int idEleve) { this.idEleve = idEleve; }

    public int getIdMoniteur() { return idMoniteur; }
    public void setIdMoniteur(int idMoniteur) { this.idMoniteur = idMoniteur; }

    public int getIdVehicule() { return idVehicule; }
    public void setIdVehicule(int idVehicule) { this.idVehicule = idVehicule; }
}
