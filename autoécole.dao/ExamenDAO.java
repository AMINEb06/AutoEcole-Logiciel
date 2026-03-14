package autoécole.dao;

import autoécole.model.Examen;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExamenDAO {

    // Ajouter un examen
    public static void ajouter(Examen e) {
        String sql = "INSERT INTO examen(id_eleve,type_examen,date_examen,resultat) VALUES(?,?,?,?)";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, e.getIdEleve());
            ps.setString(2, e.getTypeExamen());
            ps.setDate(3, e.getDateExamen() != null ? Date.valueOf(e.getDateExamen()) : null);
            ps.setString(4, e.getResultat());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    e.setIdExamen(rs.getInt(1));
                }
            }

        } catch(Exception ex){
            System.out.println("Erreur ajouter examen : " + ex.getMessage());
        }
    }

    // Modifier un examen
    public static void modifier(Examen e) {
        String sql = "UPDATE examen SET id_eleve=?, type_examen=?, date_examen=?, resultat=? WHERE id=?";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, e.getIdEleve());
            ps.setString(2, e.getTypeExamen());
            ps.setDate(3, e.getDateExamen() != null ? Date.valueOf(e.getDateExamen()) : null);
            ps.setString(4, e.getResultat());
            ps.setInt(5, e.getIdExamen());
            ps.executeUpdate();

        } catch(Exception ex){
            System.out.println("Erreur modifier examen : " + ex.getMessage());
        }
    }

    // Supprimer un examen
    public static void supprimer(int id) {
        String sql = "DELETE FROM examen WHERE id=?";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch(Exception ex){
            System.out.println("Erreur supprimer examen : " + ex.getMessage());
        }
    }

    // Lister tous les examens
    public static List<Examen> liste() {
        List<Examen> list = new ArrayList<>();
        String sql = "SELECT * FROM examen ORDER BY id DESC";
        try (Connection cnx = Connexion.getConnexion();
             Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while(rs.next()) {
                Examen e = new Examen();
                e.setIdExamen(rs.getInt("id"));
                e.setIdEleve(rs.getInt("id_eleve"));
                e.setTypeExamen(rs.getString("type_examen"));
                Date d = rs.getDate("date_examen");
                e.setDateExamen(d != null ? d.toLocalDate() : null);
                e.setResultat(rs.getString("resultat"));
                list.add(e);
            }

        } catch(Exception ex){
            System.out.println("Erreur liste examen : " + ex.getMessage());
        }
        return list;
    }

    // Trouver un examen par ID
    public static Examen trouverParId(int id) {
        Examen e = null;
        String sql = "SELECT * FROM examen WHERE id=?";
        try (Connection cnx = Connexion.getConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    e = new Examen();
                    e.setIdExamen(rs.getInt("id"));
                    e.setIdEleve(rs.getInt("id_eleve"));
                    e.setTypeExamen(rs.getString("type_examen"));
                    Date d = rs.getDate("date_examen");
                    e.setDateExamen(d != null ? d.toLocalDate() : null);
                    e.setResultat(rs.getString("resultat"));
                }
            }

        } catch(Exception ex){
            System.out.println("Erreur trouverParId examen : " + ex.getMessage());
        }
        return e;
    }
}
