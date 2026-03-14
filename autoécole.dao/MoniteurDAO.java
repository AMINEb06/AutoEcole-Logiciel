package autoécole.dao;

import autoécole.model.Moniteur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoniteurDAO {

    // Ajouter un moniteur
    public static boolean ajouter(Moniteur m) {
        if (emailOuTelExisteDansToutesTables(0, m.getEmail(), m.getTelephone())) {
            System.out.println("Erreur : email ou téléphone déjà utilisé !");
            return false;
        }
        String sql = "INSERT INTO moniteur(nom, prenom, email, telephone) VALUES(?, ?, ?, ?)";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getPrenom());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelephone());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Erreur ajouter : " + ex.getMessage());
        }
        return false;
    }

    // Modifier un moniteur
    public static boolean modifier(Moniteur m) {
        if (emailOuTelExisteDansToutesTables(m.getId(), m.getEmail(), m.getTelephone())) {
            System.out.println("Erreur : email ou téléphone déjà utilisé !");
            return false;
        }
        String sql = "UPDATE moniteur SET nom=?, prenom=?, email=?, telephone=? WHERE id=?";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getPrenom());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelephone());
            ps.setInt(5, m.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("Erreur modifier : " + ex.getMessage());
        }
        return false;
    }

    // Supprimer un moniteur
    public static void supprimer(int id) {
        String sql = "DELETE FROM moniteur WHERE id=?";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Erreur supprimer : " + ex.getMessage());
        }
    }

    // Lister tous les moniteurs
    public static List<Moniteur> liste() {
        List<Moniteur> liste = new ArrayList<>();
        String sql = "SELECT * FROM moniteur ORDER BY id DESC";
        try (Connection cnx = Connexion.getConnexion(); PreparedStatement ps = cnx.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Moniteur m = new Moniteur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone")
                );
                liste.add(m);
            }
        } catch (Exception ex) {
            System.out.println("Erreur liste : " + ex.getMessage());
        }
        return liste;
    }

    // Vérifie si un email ou téléphone existe déjà dans moniteur ou eleve
    public static boolean emailOuTelExisteDansToutesTables(int idMoniteur, String email, String telephone) {
        String sqlMoniteur = "SELECT COUNT(*) FROM moniteur WHERE (email=? OR telephone=?) AND id<>?";
        String sqlEleve = "SELECT COUNT(*) FROM eleve WHERE email=? OR telephone=?";
        try (Connection cnx = Connexion.getConnexion()) {
            // Vérifier dans moniteur
            try (PreparedStatement ps = cnx.prepareStatement(sqlMoniteur)) {
                ps.setString(1, email);
                ps.setString(2, telephone);
                ps.setInt(3, idMoniteur);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return true;
                }
            }
            // Vérifier dans eleve
            try (PreparedStatement ps2 = cnx.prepareStatement(sqlEleve)) {
                ps2.setString(1, email);
                ps2.setString(2, telephone);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next() && rs2.getInt(1) > 0) return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true; // sécurité
        }
        return false;
    }
}
