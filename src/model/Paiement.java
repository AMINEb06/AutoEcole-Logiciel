package autoécole.model;

import java.util.Date;

public class Paiement {
    private int id;
    private int idEleve;
    private double montant;
    private Date date;
    private static int compteur = 1;

    public Paiement(int idEleve, double montant, Date date) {
        this.id = compteur++;
        this.idEleve = idEleve;
        this.montant = montant;
        this.date = date;
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getIdEleve() { return idEleve; }
    public double getMontant() { return montant; }
    public Date getDate() { return date; }

    // --- Setters ---
    public void setId(int id) { this.id = id; } // nécessaire pour modifier après récupération BDD
    public void setIdEleve(int idEleve) { this.idEleve = idEleve; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setDate(Date date) { this.date = date; }
}
