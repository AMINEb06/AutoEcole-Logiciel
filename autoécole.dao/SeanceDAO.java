package autoécole.dao;

import autoécole.model.Seance;
import java.sql.*;
import java.util.ArrayList;

public class SeanceDAO {

    public static void ajouter(Seance s) {
        String sql = "INSERT INTO seance(date,heure,idEleve,idMoniteur,idVehicule) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = Connexion.getConnexion().prepareStatement(sql);
            ps.setString(1, s.getDate());
            ps.setString(2, s.getHeure());
            ps.setInt(3, s.getIdEleve());
            ps.setInt(4, s.getIdMoniteur());
            ps.setInt(5, s.getIdVehicule());
            ps.executeUpdate();
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    public static void modifier(Seance s) {
        String sql = "UPDATE seance SET date=?, heure=?, idEleve=?, idMoniteur=?, idVehicule=? WHERE idSeance=?";
        try {
            PreparedStatement ps = Connexion.getConnexion().prepareStatement(sql);
            ps.setString(1, s.getDate());
            ps.setString(2, s.getHeure());
            ps.setInt(3, s.getIdEleve());
            ps.setInt(4, s.getIdMoniteur());
            ps.setInt(5, s.getIdVehicule());
            ps.setInt(6, s.getIdSeance());
            ps.executeUpdate();
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    public static void supprimer(int id) {
        String sql = "DELETE FROM seance WHERE idSeance=?";
        try {
            PreparedStatement ps = Connexion.getConnexion().prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    public static ArrayList<Seance> liste() {
        ArrayList<Seance> list = new ArrayList<>();
        try {
            Statement st = Connexion.getConnexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM seance");
            while(rs.next()) {
                list.add(new Seance(
                    rs.getInt("idSeance"),
                    rs.getString("date"),
                    rs.getString("heure"),
                    rs.getInt("idEleve"),
                    rs.getInt("idMoniteur"),
                    rs.getInt("idVehicule")
                ));
            }
        } catch(Exception ex) { ex.printStackTrace(); }
        return list;
    }
}
