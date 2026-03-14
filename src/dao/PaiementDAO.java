package autoécole.dao;

import autoécole.model.Paiement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaiementDAO {

    // --- Lister tous les paiements ---
    public static List<Paiement> liste() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement ORDER BY id ASC";
        try (Connection cnx = Connexion.getConnexion();
             Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Paiement p = new Paiement(
                        rs.getInt("id_eleve"),
                        rs.getDouble("montant"),
                        rs.getDate("date_paiement")
                );
                p.setId(rs.getInt("id"));
                paiements.add(p);
            }

        } catch (Exception e) {
            System.out.println("Erreur liste : " + e.getMessage());
        }
        return paiements;
    }

    // --- Ajouter un paiement ---
    public static void ajouter(Paiement p) {
        String sql = "INSERT INTO paiement (id_eleve, montant, date_paiement) VALUES (?, ?, ?)";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, p.getIdEleve());
            ps.setDouble(2, p.getMontant());
            ps.setDate(3, new java.sql.Date(p.getDate().getTime()));
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Erreur ajouter : " + e.getMessage());
        }
    }

    // --- Modifier un paiement ---
    public static void modifier(Paiement p) {
        String sql = "UPDATE paiement SET id_eleve=?, montant=?, date_paiement=? WHERE id=?";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, p.getIdEleve());
            ps.setDouble(2, p.getMontant());
            ps.setDate(3, new java.sql.Date(p.getDate().getTime()));
            ps.setInt(4, p.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Erreur modifier : " + e.getMessage());
        }
    }

    // --- Supprimer un paiement ---
    public static void supprimer(int id) {
        String sql = "DELETE FROM paiement WHERE id=?";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Erreur supprimer : " + e.getMessage());
        }
    }

    // --- Trouver un paiement par son ID ---
    public static Paiement trouverParId(int id) {
        Paiement p = null;
        String sql = "SELECT * FROM paiement WHERE id=?";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Paiement(
                            rs.getInt("id_eleve"),
                            rs.getDouble("montant"),
                            rs.getDate("date_paiement")
                    );
                    p.setId(rs.getInt("id"));
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur trouverParId : " + e.getMessage());
        }
        return p;
    }
}
