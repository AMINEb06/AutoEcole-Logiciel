package autoécole.présentation;

import autoécole.dao.EleveDAO;
import autoécole.dao.PaiementDAO;
import autoécole.model.Eleve;
import autoécole.model.Paiement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FramePaiement extends JFrame {

    private JComboBox<String> comboEleves;
    private JTextField txtMontant;
    private JSpinner spinnerDate;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnReinitialiser, btnRechercher, btnAfficherTous;
    private JTable tablePaiement;
    private DefaultTableModel modelTable;
    private List<Paiement> listePaiements;
    private List<Eleve> listeEleves;
    private JLabel labelStatut;
    private JTextField txtRecherche;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public FramePaiement() {
        // Récupération des élèves et paiements depuis la BDD
        listeEleves = EleveDAO.liste();
        listePaiements = PaiementDAO.liste();

        initialiserInterface();
    }

    private void initialiserInterface() {
        setTitle("Gestion des Paiements");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Recherche ---
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtRecherche = new JTextField(25);
        panelRecherche.add(new JLabel("Recherche:"));
        panelRecherche.add(txtRecherche);

        btnRechercher = creerBouton("Rechercher", new Color(63, 81, 181), Color.WHITE);
        btnRechercher.addActionListener(e -> rechercherPaiement());
        panelRecherche.add(btnRechercher);

        btnAfficherTous = creerBouton("Afficher tous", new Color(103, 58, 183), Color.WHITE);
        btnAfficherTous.addActionListener(e -> mettreAJourTable());
        panelRecherche.add(btnAfficherTous);

        add(panelRecherche, BorderLayout.NORTH);

        // --- Table ---
        modelTable = new DefaultTableModel(new String[]{"Élève", "Montant", "Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePaiement = new JTable(modelTable);
        tablePaiement.setRowHeight(30);

        tablePaiement.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) chargerPaiementSelectionne();
        });

        add(new JScrollPane(tablePaiement), BorderLayout.CENTER);

        // --- Formulaire ---
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        comboEleves = new JComboBox<>();
        remplirComboEleves();

        txtMontant = new JTextField(10);

        spinnerDate = new JSpinner(new SpinnerDateModel());
        spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "yyyy-MM-dd"));

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Élève:"), gbc);
        gbc.gridx = 1; form.add(comboEleves, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Montant:"), gbc);
        gbc.gridx = 1; form.add(txtMontant, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1; form.add(spinnerDate, gbc);

        JPanel panelBoutons = new JPanel();

        btnAjouter = creerBouton("Ajouter", new Color(76, 175, 80), Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterPaiement());

        btnModifier = creerBouton("Modifier", new Color(63, 81, 181), Color.WHITE);
        btnModifier.addActionListener(e -> modifierPaiement());

        btnSupprimer = creerBouton("Supprimer", new Color(244, 67, 54), Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerPaiement());

        btnReinitialiser = creerBouton("Réinitialiser", new Color(158, 158, 158), Color.WHITE);
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnReinitialiser);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(panelBoutons, gbc);

        labelStatut = new JLabel();
        gbc.gridy = 4;
        form.add(labelStatut, gbc);

        add(form, BorderLayout.SOUTH);

        mettreAJourTable();
    }

    private JButton creerBouton(String txt, Color bg, Color fg) {
        JButton b = new JButton(txt);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        return b;
    }

    private void remplirComboEleves() {
        comboEleves.removeAllItems();
        for (Eleve e : listeEleves) {
            comboEleves.addItem(e.getIdEleve() + " - " + e.getNom() + " " + e.getPrenom());
        }
    }

    private void ajouterPaiement() {
        try {
            int idEleve = Integer.parseInt(comboEleves.getSelectedItem().toString().split(" - ")[0]);

            String montantTxt = txtMontant.getText().trim();
            double montant = Double.parseDouble(montantTxt);

            Date date = (Date) spinnerDate.getValue();

            Paiement p = new Paiement(idEleve, montant, date);
            PaiementDAO.ajouter(p);
            listePaiements = PaiementDAO.liste();

            mettreAJourTable();
            reinitialiserFormulaire();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void modifierPaiement() {
        int ligne = tablePaiement.getSelectedRow();
        if (ligne == -1) return;

        Paiement p = listePaiements.get(ligne);

        int idEleve = Integer.parseInt(comboEleves.getSelectedItem().toString().split(" - ")[0]);
        double montant = Double.parseDouble(txtMontant.getText().trim());
        Date date = (Date) spinnerDate.getValue();

        p.setIdEleve(idEleve);
        p.setMontant(montant);
        p.setDate(date);

        PaiementDAO.modifier(p);
        listePaiements = PaiementDAO.liste();

        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void supprimerPaiement() {
        int ligne = tablePaiement.getSelectedRow();
        if (ligne == -1) return;

        Paiement p = listePaiements.get(ligne);
        PaiementDAO.supprimer(p.getId());
        listePaiements = PaiementDAO.liste();

        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void reinitialiserFormulaire() {
        if (comboEleves.getItemCount() > 0) comboEleves.setSelectedIndex(0);
        txtMontant.setText("");
        spinnerDate.setValue(new Date());
        tablePaiement.clearSelection();
    }

    private void mettreAJourTable() {
        modelTable.setRowCount(0);
        for (Paiement p : listePaiements) {
            Eleve e = EleveDAO.trouverParId(p.getIdEleve());
            String nomEleve = e != null ? e.getNom() + " " + e.getPrenom() : "Inconnu";
            modelTable.addRow(new Object[]{
                    nomEleve,
                    p.getMontant() + " DA",
                    sdf.format(p.getDate())
            });
        }
        labelStatut.setText("Total : " + listePaiements.size() + " paiement(s)");
    }

    private void chargerPaiementSelectionne() {
        int ligne = tablePaiement.getSelectedRow();
        if (ligne == -1) return;

        Paiement p = listePaiements.get(ligne);
        for (int i = 0; i < comboEleves.getItemCount(); i++) {
            if (comboEleves.getItemAt(i).startsWith(p.getIdEleve() + " -")) {
                comboEleves.setSelectedIndex(i);
                break;
            }
        }

        txtMontant.setText(String.valueOf(p.getMontant()));
        spinnerDate.setValue(p.getDate());
    }

    private void rechercherPaiement() {
        String r = txtRecherche.getText().toLowerCase().trim();
        if (r.isEmpty()) {
            mettreAJourTable();
            return;
        }

        modelTable.setRowCount(0);
        for (Paiement p : listePaiements) {
            Eleve e = EleveDAO.trouverParId(p.getIdEleve());
            String nomEleve = e != null ? e.getNom() + " " + e.getPrenom() : "Inconnu";
            if (nomEleve.toLowerCase().contains(r) ||
                String.valueOf(p.getMontant()).contains(r) ||
                sdf.format(p.getDate()).contains(r)) {
                modelTable.addRow(new Object[]{
                        nomEleve,
                        p.getMontant() + " DA",
                        sdf.format(p.getDate())
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FramePaiement().setVisible(true));
    }
}
