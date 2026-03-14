package autoécole.présentation;

import autoécole.dao.LoginDAO;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JButton btnForgot;

    public LoginForm() {
        setTitle("Connexion - Auto École");
        setSize(430, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Color rouge = new Color(220, 20, 60);
        Color blanc = Color.WHITE;
        getContentPane().setBackground(blanc);

        JLabel title = new JLabel("Auto École - Connexion", SwingConstants.CENTER);
        title.setBounds(40, 30, 340, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(rouge);
        add(title);

        JLabel lblUser = new JLabel("Nom d'utilisateur :");
        lblUser.setBounds(60, 120, 150, 25);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(Color.BLACK);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(60, 150, 300, 40);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUser.setHorizontalAlignment(JTextField.CENTER);
        add(txtUser);

        JLabel lblPass = new JLabel("Mot de passe :");
        lblPass.setBounds(60, 220, 150, 25);
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(Color.BLACK);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(60, 250, 300, 40);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPass.setHorizontalAlignment(JPasswordField.CENTER);
        add(txtPass);

        btnLogin = new JButton("Se connecter");
        btnLogin.setBounds(120, 340, 180, 45);
        btnLogin.setBackground(rouge);
        btnLogin.setForeground(blanc);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        add(btnLogin);

        btnForgot = new JButton("Mot de passe oublié ?");
        btnForgot.setBounds(120, 400, 200, 30);
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnForgot.setForeground(rouge);
        btnForgot.setFocusPainted(false);
        btnForgot.setBorderPainted(false);
        btnForgot.setContentAreaFilled(false);
        add(btnForgot);

        // Action boutons
        btnLogin.addActionListener(e -> login());
        txtUser.addActionListener(e -> login());
        txtPass.addActionListener(e -> login());

        btnForgot.addActionListener(e -> {
            DialogModifierMotDePasse d = new DialogModifierMotDePasse(this);
            d.setVisible(true);
        });
    }

    private void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if(user.isEmpty() || pass.isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if(LoginDAO.validerLogin(user, pass)){
                new MainFrame().setVisible(true); // Fenêtre principale
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Utilisateur ou mot de passe incorrect !", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
