package autoécole.model;

public class Eleve {

    private int idEleve;
    private String nom;
    private String prenom;
    private String email;    
    private String telephone;
    private String dateNaiss;

    public Eleve() {}

    public Eleve(String nom, String prenom, String email, String telephone, String dateNaiss) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.dateNaiss = dateNaiss;
    }

    // --- Getters et Setters ---
    public int getIdEleve() { return idEleve; }
    public void setIdEleve(int idEleve) { this.idEleve = idEleve; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getDateNaiss() { return dateNaiss; }
    public void setDateNaiss(String dateNaiss) { this.dateNaiss = dateNaiss; }
}
