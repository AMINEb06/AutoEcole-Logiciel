package autoécole.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {

    private static final String URL = "jdbc:mysql://localhost:3306/auto_ecole?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection cnx;

    public static Connection getConnexion() {
        try {
            if (cnx == null || cnx.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                cnx = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion MySQL réussie !");
            }
        } catch (Exception e) {
            e.printStackTrace(); // <-- affiche l'erreur complète
            System.out.println("Erreur connexion : " + e.getMessage());
        }

        return cnx;
    }
}
