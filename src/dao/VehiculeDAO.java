package autoécole.dao;

import autoécole.model.Vehicule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {
    private static Connection cnx = Connexion.getConnexion();

    // Lister tous les véhicules
    public static List<Vehicule> liste() {
        List<Vehicule> vehicules = new ArrayList<>();
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM vehicule ORDER BY id ASC");
            while(rs.next()){
                Vehicule v = new Vehicule(
                    rs.getInt("id"),
                    rs.getString("marque"),
                    rs.getString("modele"),
                    rs.getString("immatriculation")
                );
                vehicules.add(v);
            }
        } catch(Exception e){
            System.out.println("Erreur liste : " + e.getMessage());
        }
        return vehicules;
    }

    // Vérifier si une immatriculation existe déjà
    // idExclu = 0 pour ajout, id du véhicule pour modification
    public static boolean existeImmatriculation(String immat, int idExclu) {
    // idExclu = 0 si ajout, sinon l'ID du véhicule qu'on modifie
    String sql = "SELECT COUNT(*) FROM vehicule WHERE immatriculation = ? AND id <> ?";
    try (PreparedStatement ps = cnx.prepareStatement(sql)) {
        ps.setString(1, immat);
        ps.setInt(2, idExclu);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch(Exception e) {
        System.out.println("Erreur existeImmatriculation : " + e.getMessage());
    }
    return false;
}


    // Ajouter un véhicule
    public static void ajouter(Vehicule v) {
        // Vérifier unicité
        if(existeImmatriculation(v.getImmatriculation(), 0)) {
            System.out.println("Erreur : immatriculation déjà utilisée !");
            return;
        }

        try {
            PreparedStatement ps = cnx.prepareStatement(
                "INSERT INTO vehicule (marque, modele, immatriculation) VALUES (?, ?, ?)"
            );
            ps.setString(1, v.getMarque());
            ps.setString(2, v.getModele());
            ps.setString(3, v.getImmatriculation());
            ps.executeUpdate();
        } catch(Exception e){
            System.out.println("Erreur ajouter : " + e.getMessage());
        }
    }

    // Modifier un véhicule
    public static void modifier(Vehicule v) {
        // Vérifier unicité
        if(existeImmatriculation(v.getImmatriculation(), v.getId())) {
            System.out.println("Erreur : immatriculation déjà utilisée !");
            return;
        }

        try {
            PreparedStatement ps = cnx.prepareStatement(
                "UPDATE vehicule SET marque=?, modele=?, immatriculation=? WHERE id=?"
            );
            ps.setString(1, v.getMarque());
            ps.setString(2, v.getModele());
            ps.setString(3, v.getImmatriculation());
            ps.setInt(4, v.getId());
            ps.executeUpdate();
        } catch(Exception e){
            System.out.println("Erreur modifier : " + e.getMessage());
        }
    }

    // Supprimer un véhicule
    public static void supprimer(int id){
        try {
            PreparedStatement ps = cnx.prepareStatement("DELETE FROM vehicule WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch(Exception e){
            System.out.println("Erreur supprimer : " + e.getMessage());
        }
    }
    
    
}
