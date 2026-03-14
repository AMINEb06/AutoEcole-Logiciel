package autoécole.model;

import java.time.LocalDate;

public class Examen {
    private int idExamen;
    private int idEleve;
    private String typeExamen; // conserver comme String ("code","creno","circulation")
    private LocalDate dateExamen;
    private String resultat; // "reussi","echoue"

    public Examen() {}

    public Examen(int idEleve, String typeExamen, LocalDate dateExamen, String resultat){
        this.idEleve = idEleve;
        this.typeExamen = typeExamen;
        this.dateExamen = dateExamen;
        this.resultat = resultat;
    }

    public int getIdExamen() { return idExamen; }
    public void setIdExamen(int idExamen) { this.idExamen = idExamen; }

    public int getIdEleve() { return idEleve; }
    public void setIdEleve(int idEleve) { this.idEleve = idEleve; }

    public String getTypeExamen() { return typeExamen; }
    public void setTypeExamen(String typeExamen) { this.typeExamen = typeExamen; }

    public LocalDate getDateExamen() { return dateExamen; }
    public void setDateExamen(LocalDate dateExamen) { this.dateExamen = dateExamen; }

    public String getResultat() { return resultat; }
    public void setResultat(String resultat) { this.resultat = resultat; }
}
