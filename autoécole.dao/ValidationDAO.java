package autoécole.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ValidationDAO {

    // Vérifie si email ou téléphone existe déjà dans eleve ou moniteur
    public static boolean existeEmailOuTelephone(String email, String telephone) {
        String sqlEleve = "SELECT COUNT(*) FROM eleve WHERE email=? OR telephone=?";
        String sqlMoniteur = "SELECT COUNT(*) FROM moniteur WHERE email=? OR telephone=?";

        try (Connection cnx = Connexion.getConnexion()) {
            // Vérifier dans Eleve
            try (PreparedStatement ps = cnx.prepareStatement(sqlEleve)) {
                ps.setString(1, email);
                ps.setString(2, telephone);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return true;
                }
            }
            // Vérifier dans Moniteur
            try (PreparedStatement ps = cnx.prepareStatement(sqlMoniteur)) {
                ps.setString(1, email);
                ps.setString(2, telephone);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return true;
                }
            }
        } catch (Exception ex) {
            System.out.println("Erreur ValidationDAO: " + ex.getMessage());
        }
        return false;
    }
}
