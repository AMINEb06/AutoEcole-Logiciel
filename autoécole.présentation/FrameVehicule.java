package autoécole.présentation;

import autoécole.dao.VehiculeDAO;
import autoécole.model.Vehicule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class FrameVehicule extends JFrame {

    private JTextField txtMarque, txtModele, txtImmat, txtRecherche;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnReinitialiser, btnRechercher, btnAfficherTous;
    private JTable tableVehicule;
    private DefaultTableModel modelTable;
    private List<Vehicule> listeVehicules;
    private JLabel labelStatut;

    public FrameVehicule() {
        listeVehicules = VehiculeDAO.liste();
        initialiserInterface();
    }

    private void initialiserInterface() {
        setTitle("Gestion des Véhicules");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        getContentPane().setBackground(new Color(250,250,250));

        // --- Panel recherche ---
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        txtRecherche = new JTextField(25);
        panelRecherche.add(new JLabel("Recherche:"));
        panelRecherche.add(txtRecherche);

        btnRechercher = creerBoutonModerne("Rechercher", new Color(63,81,181), Color.WHITE);
        btnRechercher.addActionListener(e -> rechercherVehicule());
        panelRecherche.add(btnRechercher);

        btnAfficherTous = creerBoutonModerne("Afficher tous", new Color(103,58,183), Color.WHITE);
        btnAfficherTous.addActionListener(e -> afficherTousVehicules());
        panelRecherche.add(btnAfficherTous);

        add(panelRecherche, BorderLayout.NORTH);

        // --- Table ---
        String[] colonnes = {"ID","Marque","Modèle","Immatriculation"};
        modelTable = new DefaultTableModel(colonnes,0){ public boolean isCellEditable(int r,int c){return false;} };
        tableVehicule = new JTable(modelTable);
        tableVehicule.setRowHeight(30);
        tableVehicule.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Masquer ID
        tableVehicule.getColumnModel().getColumn(0).setMinWidth(0);
        tableVehicule.getColumnModel().getColumn(0).setMaxWidth(0);
        tableVehicule.getColumnModel().getColumn(0).setWidth(0);
        tableVehicule.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane scroll = new JScrollPane(tableVehicule);
        add(scroll, BorderLayout.CENTER);

        tableVehicule.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) chargerVehiculeSelectionne();
        });

        // --- Formulaire ---
        JPanel panelFormulaire = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); gbc.anchor = GridBagConstraints.WEST;

        txtMarque = new JTextField(15);
        txtModele = new JTextField(15);
        txtImmat = new JTextField(15);

        gbc.gridx=0; gbc.gridy=0; panelFormulaire.add(new JLabel("Marque:"), gbc);
        gbc.gridx=1; panelFormulaire.add(txtMarque, gbc);
        gbc.gridx=0; gbc.gridy=1; panelFormulaire.add(new JLabel("Modèle:"), gbc);
        gbc.gridx=1; panelFormulaire.add(txtModele, gbc);
        gbc.gridx=0; gbc.gridy=2; panelFormulaire.add(new JLabel("Immatriculation:"), gbc);
        gbc.gridx=1; panelFormulaire.add(txtImmat, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        btnAjouter = creerBoutonModerne("Ajouter", new Color(76,175,80), Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterVehicule());
        btnModifier = creerBoutonModerne("Modifier", new Color(63,81,181), Color.WHITE);
        btnModifier.addActionListener(e -> modifierVehicule());
        btnSupprimer = creerBoutonModerne("Supprimer", new Color(244,67,54), Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerVehicule());
        btnReinitialiser = creerBoutonModerne("Réinitialiser", new Color(158,158,158), Color.WHITE);
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());
        panelBoutons.add(btnAjouter); panelBoutons.add(btnModifier); panelBoutons.add(btnSupprimer); panelBoutons.add(btnReinitialiser);

        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2; panelFormulaire.add(panelBoutons, gbc);

        labelStatut = new JLabel("Total: 0 véhicule(s)");
        gbc.gridy=4; panelFormulaire.add(labelStatut, gbc);

        add(panelFormulaire, BorderLayout.SOUTH);

        mettreAJourTable();
    }

    private JButton creerBoutonModerne(String texte, Color bg, Color fg){
        JButton b = new JButton(texte); b.setBackground(bg); b.setForeground(fg); b.setFocusPainted(false);
        return b;
    }

    private void ajouterVehicule() {
    String marque = txtMarque.getText().trim();
    String modele = txtModele.getText().trim();
    String immat = txtImmat.getText().trim();

    if(marque.isEmpty() || modele.isEmpty() || immat.isEmpty()){
        JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires !");
        return;
    }

    // Vérifier unicité
    if(VehiculeDAO.existeImmatriculation(immat, 0)){
        JOptionPane.showMessageDialog(this, "Erreur : cette immatriculation existe déjà !");
        return;
    }

    Vehicule v = new Vehicule(0, marque, modele, immat);
    VehiculeDAO.ajouter(v);
    listeVehicules = VehiculeDAO.liste();
    mettreAJourTable();
    reinitialiserFormulaire();
}

private void modifierVehicule() {
    int ligne = tableVehicule.getSelectedRow();
    if(ligne==-1) return;
    Vehicule v = listeVehicules.get(ligne);

    String marque = txtMarque.getText().trim();
    String modele = txtModele.getText().trim();
    String immat = txtImmat.getText().trim();

    if(marque.isEmpty() || modele.isEmpty() || immat.isEmpty()){
        JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires !");
        return;
    }

    // Vérifier unicité pour modification
    if(VehiculeDAO.existeImmatriculation(immat, v.getId())){
        JOptionPane.showMessageDialog(this, "Erreur : cette immatriculation existe déjà !");
        return;
    }

    v.setMarque(marque);
    v.setModele(modele);
    v.setImmatriculation(immat);

    VehiculeDAO.modifier(v);
    listeVehicules = VehiculeDAO.liste();
    mettreAJourTable();
    reinitialiserFormulaire();
}


    

    private void supprimerVehicule() {
        int ligne = tableVehicule.getSelectedRow();
        if(ligne==-1) return;
        int id = listeVehicules.get(ligne).getId();
        VehiculeDAO.supprimer(id);
        listeVehicules = VehiculeDAO.liste();
        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void reinitialiserFormulaire(){
        txtMarque.setText(""); txtModele.setText(""); txtImmat.setText("");
        tableVehicule.clearSelection();
    }

    private void mettreAJourTable(){
        modelTable.setRowCount(0);
        for(Vehicule v : listeVehicules){
            modelTable.addRow(new Object[]{v.getId(), v.getMarque(), v.getModele(), v.getImmatriculation()});
        }
        labelStatut.setText("Total: "+listeVehicules.size()+" véhicule(s)");
    }

    private void chargerVehiculeSelectionne(){
        int ligne = tableVehicule.getSelectedRow();
        if(ligne==-1) return;
        Vehicule v = listeVehicules.get(ligne);
        txtMarque.setText(v.getMarque());
        txtModele.setText(v.getModele());
        txtImmat.setText(v.getImmatriculation());
    }

    private void rechercherVehicule(){
        String r = txtRecherche.getText().toLowerCase();
        if(r.isEmpty()){ afficherTousVehicules(); return; }

        List<Vehicule> res = listeVehicules.stream().filter(v ->
                v.getMarque().toLowerCase().contains(r) ||
                v.getModele().toLowerCase().contains(r) ||
                v.getImmatriculation().toLowerCase().contains(r)
        ).toList();

        modelTable.setRowCount(0);
        for(Vehicule v : res){
            modelTable.addRow(new Object[]{v.getId(), v.getMarque(), v.getModele(), v.getImmatriculation()});
        }
        labelStatut.setText("Total: "+res.size()+" véhicule(s)");
    }

    private void afficherTousVehicules(){ 
        listeVehicules = VehiculeDAO.liste();
        mettreAJourTable();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new FrameVehicule().setVisible(true));
    }
}
