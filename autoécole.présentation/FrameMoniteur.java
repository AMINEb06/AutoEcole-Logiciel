package autoécole.présentation;

import autoécole.dao.MoniteurDAO;
import autoécole.model.Moniteur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrameMoniteur extends JFrame {

    private JTextField txtNom, txtPrenom, txtTelephone, txtEmail, txtRecherche;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnReinitialiser, btnRechercher, btnAfficherTous;
    private JTable tableMoniteur;
    private DefaultTableModel modelTable;
    private List<Moniteur> listeMoniteurs;
    private JLabel labelStatut;

    public FrameMoniteur() {
        listeMoniteurs = MoniteurDAO.liste();
        initialiserInterface();
    }

    private void initialiserInterface() {
        setTitle("Gestion des Moniteurs");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(250, 250, 250));

        // Panel recherche
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtRecherche = new JTextField(25);
        panelRecherche.add(new JLabel("Recherche:"));
        panelRecherche.add(txtRecherche);

        btnRechercher = creerBoutonModerne("Rechercher", new Color(63, 81, 181), Color.WHITE);
        btnRechercher.addActionListener(e -> rechercherMoniteur());
        panelRecherche.add(btnRechercher);

        btnAfficherTous = creerBoutonModerne("Afficher tous", new Color(103, 58, 183), Color.WHITE);
        btnAfficherTous.addActionListener(e -> afficherTousLesMoniteurs());
        panelRecherche.add(btnAfficherTous);

        add(panelRecherche, BorderLayout.NORTH);

        // Table
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone", "Email"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableMoniteur = new JTable(modelTable);
        tableMoniteur.setRowHeight(30);
        tableMoniteur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Masquer ID
        tableMoniteur.getColumnModel().getColumn(0).setMinWidth(0);
        tableMoniteur.getColumnModel().getColumn(0).setMaxWidth(0);
        tableMoniteur.getColumnModel().getColumn(0).setWidth(0);
        tableMoniteur.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane scroll = new JScrollPane(tableMoniteur);
        add(scroll, BorderLayout.CENTER);

        tableMoniteur.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) chargerMoniteurSelectionne();
        });

        // Formulaire
        JPanel panelFormulaire = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtNom = new JTextField(15);
        txtPrenom = new JTextField(15);
        txtTelephone = new JTextField(15);
        txtEmail = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; panelFormulaire.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtNom, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelFormulaire.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtPrenom, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelFormulaire.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtTelephone, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelFormulaire.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtEmail, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAjouter = creerBoutonModerne("Ajouter", new Color(76, 175, 80), Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterMoniteur());
        btnModifier = creerBoutonModerne("Modifier", new Color(63, 81, 181), Color.WHITE);
        btnModifier.addActionListener(e -> modifierMoniteur());
        btnSupprimer = creerBoutonModerne("Supprimer", new Color(244, 67, 54), Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerMoniteur());
        btnReinitialiser = creerBoutonModerne("Réinitialiser", new Color(158, 158, 158), Color.WHITE);
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());
        panelBoutons.add(btnAjouter); panelBoutons.add(btnModifier); panelBoutons.add(btnSupprimer); panelBoutons.add(btnReinitialiser);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panelFormulaire.add(panelBoutons, gbc);

        labelStatut = new JLabel("Total: 0 moniteur(s)");
        gbc.gridy = 5; panelFormulaire.add(labelStatut, gbc);

        add(panelFormulaire, BorderLayout.SOUTH);

        mettreAJourTable();
    }

    private JButton creerBoutonModerne(String texte, Color bg, Color fg) {
        JButton b = new JButton(texte);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        return b;
    }

    private void ajouterMoniteur() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String tel = txtTelephone.getText().trim();

        if(nom.isEmpty() || prenom.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nom et prénom sont obligatoires !");
            return;
        }
        if(!email.matches("^[A-Za-z][A-Za-z0-9]*@gmail\\.com$")) {

            JOptionPane.showMessageDialog(this, "Email invalide ! Exemple : exemple@domaine.com");
            return;
        }
        if(!tel.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Le numéro de téléphone doit comporter exactement 10 chiffres !");
            return;
        }

        if(MoniteurDAO.emailOuTelExisteDansToutesTables(0, email, tel)) {
            JOptionPane.showMessageDialog(this, "Ce téléphone ou email existe déjà chez un élève ou un moniteur !");
            return;
        }

        Moniteur m = new Moniteur(0, nom, prenom, email, tel);
        MoniteurDAO.ajouter(m);

        listeMoniteurs = MoniteurDAO.liste();
        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void modifierMoniteur() {
        int ligne = tableMoniteur.getSelectedRow();
        if (ligne == -1) return;

        int id = (int) modelTable.getValueAt(ligne, 0);

        Moniteur ancien = listeMoniteurs.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
        if (ancien == null) return;

        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String tel = txtTelephone.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom et prénom sont obligatoires !");
            return;
        }
        if (!email.matches("^[A-Za-z][A-Za-z0-9]*@gmail\\.com$")) {

            JOptionPane.showMessageDialog(this, "Email invalide ! Exemple : exemple@domaine.com");
            return;
        }
        if (!tel.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Le numéro de téléphone doit comporter exactement 10 chiffres !");
            return;
        }

        if (!ancien.getEmail().equals(email) || !ancien.getTelephone().equals(tel)) {
            if (MoniteurDAO.emailOuTelExisteDansToutesTables(ancien.getId(), email, tel)) {
                JOptionPane.showMessageDialog(this, "Ce téléphone ou email existe déjà chez un élève ou un moniteur !");
                return;
            }
        }

        Moniteur m = new Moniteur(ancien.getId(), nom, prenom, email, tel);
        MoniteurDAO.modifier(m);

        listeMoniteurs = MoniteurDAO.liste();
        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void supprimerMoniteur() {
        int ligne = tableMoniteur.getSelectedRow();
        if (ligne == -1) return;
        int id = listeMoniteurs.get(ligne).getId();
        MoniteurDAO.supprimer(id);
        listeMoniteurs = MoniteurDAO.liste();
        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void reinitialiserFormulaire() {
        txtNom.setText(""); txtPrenom.setText(""); txtTelephone.setText(""); txtEmail.setText("");
        tableMoniteur.clearSelection();
    }

    private void mettreAJourTable() {
        modelTable.setRowCount(0);
        for (Moniteur m : listeMoniteurs) {
            modelTable.addRow(new Object[]{ m.getId(), m.getNom(), m.getPrenom(), m.getTelephone(), m.getEmail() });
        }
        labelStatut.setText("Total: " + listeMoniteurs.size() + " moniteur(s)");
    }

    private void chargerMoniteurSelectionne() {
        int ligne = tableMoniteur.getSelectedRow();
        if (ligne == -1) return;
        Moniteur m = listeMoniteurs.get(ligne);
        txtNom.setText(m.getNom());
        txtPrenom.setText(m.getPrenom());
        txtTelephone.setText(m.getTelephone());
        txtEmail.setText(m.getEmail());
    }

    private void rechercherMoniteur() {
        String r = txtRecherche.getText().toLowerCase();
        if (r.isEmpty()) { afficherTousLesMoniteurs(); return; }
        List<Moniteur> res = new ArrayList<>();
        for (Moniteur m : listeMoniteurs) {
            if (m.getNom().toLowerCase().contains(r) || m.getPrenom().toLowerCase().contains(r) ||
                    m.getTelephone().toLowerCase().contains(r) || m.getEmail().toLowerCase().contains(r)) {
                res.add(m);
            }
        }
        modelTable.setRowCount(0);
        for (Moniteur m : res) {
            modelTable.addRow(new Object[]{ m.getId(), m.getNom(), m.getPrenom(), m.getTelephone(), m.getEmail() });
        }
        labelStatut.setText("Total: " + res.size() + " moniteur(s)");
    }

    private void afficherTousLesMoniteurs() {
        listeMoniteurs = MoniteurDAO.liste();
        mettreAJourTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrameMoniteur().setVisible(true));
    }
}
