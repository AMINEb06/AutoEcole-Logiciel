package autoécole.dao;

import autoécole.model.Eleve;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {

    // Ajouter un élève
    public static boolean ajouter(Eleve e) {
        if (existeEleve(e.getEmail(), e.getTelephone())) {
            System.out.println("Erreur : email ou téléphone déjà utilisé !");
            return false;
        }

        String sql = "INSERT INTO eleve(nom, prenom, email, telephone, dateNaissance) VALUES(?,?,?,?,?)";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getTelephone());
            ps.setString(5, e.getDateNaiss());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Erreur ajouter : " + ex.getMessage());
        }
        return false;
    }

    // Modifier un élève
    public static boolean modifier(Eleve e) {
        if (existeEleveAutre(e.getIdEleve(), e.getEmail(), e.getTelephone())) {
            System.out.println("Erreur : email ou téléphone déjà utilisé !");
            return false;
        }

        String sql = "UPDATE eleve SET nom=?, prenom=?, email=?, telephone=?, dateNaissance=? WHERE idEleve=?";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getTelephone());
            ps.setString(5, e.getDateNaiss());
            ps.setInt(6, e.getIdEleve());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Erreur modifier : " + ex.getMessage());
        }
        return false;
    }

    // Supprimer un élève
    public static void supprimer(int id) {
        String sql = "DELETE FROM eleve WHERE idEleve=?";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Erreur supprimer : " + ex.getMessage());
        }
    }

    // Trouver un élève par ID
    public static Eleve trouverParId(int id) {
        Eleve e = null;
        String sql = "SELECT * FROM eleve WHERE idEleve=?";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    e = new Eleve(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getString("dateNaissance")
                    );
                    e.setIdEleve(rs.getInt("idEleve"));
                }
            }
        } catch (Exception ex) {
            System.out.println("Erreur trouverParId EleveDAO : " + ex.getMessage());
        }
        return e;
    }

    // Lister tous les élèves
    public static List<Eleve> liste() {
        List<Eleve> list = new ArrayList<>();
        String sql = "SELECT * FROM eleve ORDER BY idEleve DESC";
        try (Connection cnx = Connexion.getConnexion(); Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Eleve e = new Eleve();
                e.setIdEleve(rs.getInt("idEleve"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setEmail(rs.getString("email"));
                e.setTelephone(rs.getString("telephone"));
                e.setDateNaiss(rs.getString("dateNaissance"));
                list.add(e);
            }
        } catch (Exception ex) {
            System.out.println("Erreur liste : " + ex.getMessage());
        }
        return list;
    }

    // Vérifie si un élève existe déjà selon email ou téléphone
    public static boolean existeEleve(String email, String telephone) {
        String sql = "SELECT COUNT(*) FROM eleve WHERE email=? OR telephone=?";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, telephone);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return rs.getInt(1) > 0;
            }
        } catch(Exception ex) {
            System.out.println("Erreur existeEleve: " + ex.getMessage());
        }
        return false;
    }

    // Vérifie si un autre élève (hors id) existe déjà selon email ou téléphone
    public static boolean existeEleveAutre(int id, String email, String telephone) {
        String sql = "SELECT COUNT(*) FROM eleve WHERE (email=? OR telephone=?) AND idEleve<>?";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, telephone);
            ps.setInt(3, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return rs.getInt(1) > 0;
            }
        } catch(Exception ex) {
            System.out.println("Erreur existeEleveAutre: " + ex.getMessage());
        }
        return false;
    }
    
    public static boolean emailOuTelExisteDansToutesTables(int idEleve, String email, String telephone) {
    // Vérifie dans la table eleve (ignore l'élève actuel)
    String sqlEleve = "SELECT COUNT(*) FROM eleve WHERE (email=? OR telephone=?) AND idEleve<>?";
    // Vérifie dans la table moniteur (pas besoin d'ignorer un ID)
    String sqlMoniteur = "SELECT COUNT(*) FROM moniteur WHERE email=? OR telephone=?";

    try (Connection cnx = Connexion.getConnexion();
         PreparedStatement psEleve = cnx.prepareStatement(sqlEleve);
         PreparedStatement psMoniteur = cnx.prepareStatement(sqlMoniteur)) {

        // Vérification dans la table eleve
        psEleve.setString(1, email);
        psEleve.setString(2, telephone);
        psEleve.setInt(3, idEleve);
        try (ResultSet rs = psEleve.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) return true;
        }

        // Vérification dans la table moniteur
        psMoniteur.setString(1, email);
        psMoniteur.setString(2, telephone);
        try (ResultSet rs = psMoniteur.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) return true;
        }

    } catch (Exception ex) {
        System.out.println("Erreur emailOuTelExisteDansToutesTables: " + ex.getMessage());
    }
    return false;
}



}
