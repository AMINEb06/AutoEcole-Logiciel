package autoécole.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    // Vérifie si le login et mot de passe sont corrects dans la BDD
    public static boolean validerLogin(String user, String pass) throws Exception {
        Connection con = Connexion.getConnexion();
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // Met à jour les identifiants dans la BDD
    public static boolean mettreAJourCredentials(String user, String newPass) throws Exception {
        Connection con = Connexion.getConnexion();
        String sql = "UPDATE users SET password=? WHERE username=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPass);
            ps.setString(2, user);
            return ps.executeUpdate() > 0;
        }
    }

    // Récupère le dernier username (optionnel)
    public static String obtenirUsername() throws Exception {
        Connection con = Connexion.getConnexion();
        String sql = "SELECT username FROM users LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("username");
        }
        return null;
    }
}
