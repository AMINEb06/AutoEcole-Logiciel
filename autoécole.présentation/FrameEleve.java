package autoécole.présentation;

import autoécole.dao.EleveDAO;
import autoécole.dao.ValidationDAO;
import autoécole.model.Eleve;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class FrameEleve extends JFrame {

    private JTextField txtNom, txtPrenom, txtTelephone, txtEmail, txtRecherche;
    private JSpinner datePicker;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnReinitialiser, btnRechercher, btnAfficherTous;
    private JTable tableEleve;
    private DefaultTableModel modelTable;
    private List<Eleve> listeEleves;
    private JLabel labelStatut;

    public FrameEleve() {
        listeEleves = EleveDAO.liste();
        initialiserInterface();
    }

    private void initialiserInterface() {
        setTitle("Gestion des Élèves");
        setSize(900, 650);
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
        btnRechercher.addActionListener(e -> rechercherEleve());
        panelRecherche.add(btnRechercher);

        btnAfficherTous = creerBoutonModerne("Afficher tous", new Color(103, 58, 183), Color.WHITE);
        btnAfficherTous.addActionListener(e -> afficherTousLesEleves());
        panelRecherche.add(btnAfficherTous);

        add(panelRecherche, BorderLayout.NORTH);

        // Table
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone", "Email", "Date de naissance"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableEleve = new JTable(modelTable);
        tableEleve.setRowHeight(30);
        tableEleve.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Masquer ID
        tableEleve.getColumnModel().getColumn(0).setMinWidth(0);
        tableEleve.getColumnModel().getColumn(0).setMaxWidth(0);
        tableEleve.getColumnModel().getColumn(0).setWidth(0);
        tableEleve.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane scroll = new JScrollPane(tableEleve);
        add(scroll, BorderLayout.CENTER);

        tableEleve.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) chargerEleveSelectionne();
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

        Calendar calMax = Calendar.getInstance();
        Date dateMax = calMax.getTime();
        Calendar calMin = Calendar.getInstance();
        calMin.add(Calendar.YEAR, -100);
        Date dateMin = calMin.getTime();

        SpinnerDateModel modelDate = new SpinnerDateModel(dateMax, dateMin, dateMax, Calendar.DAY_OF_MONTH);
        datePicker = new JSpinner(modelDate);
        datePicker.setEditor(new JSpinner.DateEditor(datePicker, "yyyy-MM-dd"));

        gbc.gridx = 0; gbc.gridy = 0; panelFormulaire.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtNom, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelFormulaire.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtPrenom, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelFormulaire.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtTelephone, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelFormulaire.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panelFormulaire.add(new JLabel("Date de naissance:"), gbc);
        gbc.gridx = 1; panelFormulaire.add(datePicker, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAjouter = creerBoutonModerne("Ajouter", new Color(76, 175, 80), Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterEleve());
        btnModifier = creerBoutonModerne("Modifier", new Color(63, 81, 181), Color.WHITE);
        btnModifier.addActionListener(e -> modifierEleve());
        btnSupprimer = creerBoutonModerne("Supprimer", new Color(244, 67, 54), Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerEleve());
        btnReinitialiser = creerBoutonModerne("Réinitialiser", new Color(158, 158, 158), Color.WHITE);
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());
        panelBoutons.add(btnAjouter); panelBoutons.add(btnModifier); panelBoutons.add(btnSupprimer); panelBoutons.add(btnReinitialiser);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; panelFormulaire.add(panelBoutons, gbc);

        labelStatut = new JLabel("Total: 0 élève(s)");
        gbc.gridy = 6; panelFormulaire.add(labelStatut, gbc);

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

    

    private void ajouterEleve() {
    try {
        String tel = txtTelephone.getText().trim();
        if (!tel.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Le numéro de téléphone doit comporter exactement 10 chiffres !");
            return;
        }

        String email = txtEmail.getText().trim();
        String regexEmail = "^[A-Za-z][A-Za-z0-9]*@gmail\\.com$";
        if (!email.matches(regexEmail)) {
            JOptionPane.showMessageDialog(this, "Email invalide ! Format attendu : exemple@domaine.com");
            return;
        }

        // Vérification doublon dans élèves et moniteurs
        if (EleveDAO.emailOuTelExisteDansToutesTables(0, email, tel)) {
            JOptionPane.showMessageDialog(this, "Ce téléphone ou email existe déjà chez un élève ou un moniteur !");
            return;
        }

        Date d = (Date) datePicker.getValue();
        String dateStr = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();

        Eleve e = new Eleve(txtNom.getText(), txtPrenom.getText(), email, tel, dateStr);
        EleveDAO.ajouter(e);

        listeEleves = EleveDAO.liste();
        mettreAJourTable();
        reinitialiserFormulaire();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
    }
}


private void modifierEleve() {
    int ligne = tableEleve.getSelectedRow();
    if (ligne == -1) return; // aucune ligne sélectionnée

    try {
        String tel = txtTelephone.getText().trim();
        String email = txtEmail.getText().trim();
        String regexEmail = "^[A-Za-z][A-Za-z0-9]*@gmail\\.com$";
        if (!email.matches(regexEmail)) {
            JOptionPane.showMessageDialog(this, "Email invalide ! Format attendu : exemple@domaine.com");
            return;
        }

        Eleve ancien = listeEleves.get(ligne);

        // Vérification doublon si email ou téléphone changent
        if (!ancien.getEmail().equals(email) || !ancien.getTelephone().equals(tel)) {
            if (EleveDAO.emailOuTelExisteDansToutesTables(ancien.getIdEleve(), email, tel)) {
                JOptionPane.showMessageDialog(this, "Ce téléphone ou email existe déjà chez un élève ou un moniteur !");
                return;
            }
        }

        Date d = (Date) datePicker.getValue();
        String dateStr = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();

        // Mettre à jour directement l'objet existant
        ancien.setNom(txtNom.getText());
        ancien.setPrenom(txtPrenom.getText());
        ancien.setEmail(email);
        ancien.setTelephone(tel);
        ancien.setDateNaiss(dateStr);

        EleveDAO.modifier(ancien); // sauvegarder dans la BDD

        listeEleves = EleveDAO.liste(); // recharger la liste
        mettreAJourTable(); // mettre à jour la table
        reinitialiserFormulaire(); // réinitialiser le formulaire

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
    }
}




    private void supprimerEleve() {
        int ligne = tableEleve.getSelectedRow();
        if (ligne == -1) return;
        int id = listeEleves.get(ligne).getIdEleve();
        EleveDAO.supprimer(id);
        listeEleves = EleveDAO.liste();
        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void reinitialiserFormulaire() {
        txtNom.setText(""); txtPrenom.setText(""); txtTelephone.setText(""); txtEmail.setText("");
        datePicker.setValue(new Date());
        tableEleve.clearSelection();
    }

    private void mettreAJourTable() {
        modelTable.setRowCount(0);
        for (Eleve e : listeEleves) {
            modelTable.addRow(new Object[]{ e.getIdEleve(), e.getNom(), e.getPrenom(), e.getTelephone(), e.getEmail(), e.getDateNaiss() });
        }
        labelStatut.setText("Total: " + listeEleves.size() + " élève(s)");
    }

    private void chargerEleveSelectionne() {
        int ligne = tableEleve.getSelectedRow();
        if (ligne == -1) return;
        Eleve e = listeEleves.get(ligne);
        txtNom.setText(e.getNom());
        txtPrenom.setText(e.getPrenom());
        txtTelephone.setText(e.getTelephone());
        txtEmail.setText(e.getEmail());
        Date date = Date.from(LocalDate.parse(e.getDateNaiss()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        datePicker.setValue(date);
    }

    private void rechercherEleve() {
        String r = txtRecherche.getText().toLowerCase();
        if (r.isEmpty()) { afficherTousLesEleves(); return; }
        List<Eleve> res = new ArrayList<>();
        for (Eleve e : listeEleves) {
            if (e.getNom().toLowerCase().contains(r) || e.getPrenom().toLowerCase().contains(r) ||
                e.getTelephone().toLowerCase().contains(r) || e.getEmail().toLowerCase().contains(r)) {
                res.add(e);
            }
        }
        modelTable.setRowCount(0);
        for (Eleve e : res) {
            modelTable.addRow(new Object[]{ e.getIdEleve(), e.getNom(), e.getPrenom(), e.getTelephone(), e.getEmail(), e.getDateNaiss() });
        }
        labelStatut.setText("Total: " + res.size() + " élève(s)");
    }

    private void afficherTousLesEleves() {
        listeEleves = EleveDAO.liste();
        mettreAJourTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrameEleve().setVisible(true));
    }
}
