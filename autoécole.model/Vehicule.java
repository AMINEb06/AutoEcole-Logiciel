package autoécole.model;

public class Vehicule {
    private int id;
    private String marque;
    private String modele;
    private String immatriculation;

    public Vehicule(int id, String marque, String modele, String immatriculation){
        this.id = id;
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
    }

    public int getId(){ return id; }
    public String getMarque(){ return marque; }
    public String getModele(){ return modele; }
    public String getImmatriculation(){ return immatriculation; }

    public void setMarque(String marque){ this.marque = marque; }
    public void setModele(String modele){ this.modele = modele; }
    public void setImmatriculation(String immatriculation){ this.immatriculation = immatriculation; }
}
