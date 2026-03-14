package autoécole.présentation;

import autoécole.dao.LoginDAO;
import javax.swing.*;
import java.awt.*;

public class DialogModifierMotDePasse extends JDialog {

    private JTextField txtUser;
    private JPasswordField txtNew;
    private JPasswordField txtConfirm;

    public DialogModifierMotDePasse(Frame parent) {
        super(parent, "Modifier le mot de passe", true);
        setSize(500, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblUser = new JLabel("Nom d'utilisateur :");
        lblUser.setBounds(30, 60, 150, 25);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(180, 60, 280, 30);
        add(txtUser);

        JLabel lblNew = new JLabel("Nouveau mot de passe :");
        lblNew.setBounds(30, 100, 150, 25);
        add(lblNew);

        txtNew = new JPasswordField();
        txtNew.setBounds(180, 100, 280, 30);
        add(txtNew);

        JLabel lblConfirm = new JLabel("Confirmer mot de passe :");
        lblConfirm.setBounds(30, 140, 150, 25);
        add(lblConfirm);

        txtConfirm = new JPasswordField();
        txtConfirm.setBounds(180, 140, 280, 30);
        add(txtConfirm);

        JButton btnCancel = new JButton("Annuler");
        btnCancel.setBounds(180, 200, 120, 35);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setOpaque(true);
        btnCancel.setBorderPainted(false);
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);

        JButton btnValider = new JButton("Modifier");
        btnValider.setBounds(340, 200, 120, 35);
        btnValider.setBackground(new Color(220, 20, 60));
        btnValider.setForeground(Color.WHITE);
        btnValider.setOpaque(true);
        btnValider.setBorderPainted(false);
        btnValider.addActionListener(e -> doModify());
        add(btnValider);
    }

    private void doModify() {
        String user = txtUser.getText().trim();
        String newPass = new String(txtNew.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        if(user.isEmpty() || newPass.isEmpty() || confirm.isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!newPass.equals(confirm)){
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if(LoginDAO.mettreAJourCredentials(user, newPass)){
                JOptionPane.showMessageDialog(this, "Mot de passe modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la mise à jour. Vérifiez le nom d'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
