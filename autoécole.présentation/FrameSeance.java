package autoécole.présentation;

import autoécole.dao.Connexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrameSeance extends JFrame {

    private JComboBox<MoniteurItem> cbMoniteur;
    private JComboBox<EleveItem> cbEleve;
    private JComboBox<VehiculeItem> cbVehicule;
    private JComboBox<String> cbTypeCours;
    private JSpinner spDate, spHeure;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnReinitialiser, btnRechercher, btnAfficherTous;
    private JTable tableSeance;
    private DefaultTableModel modelTable;
    private JLabel labelStatut;
    private JTextField txtRecherche;

    private List<Seance> listeSeances;

    public FrameSeance() {
        listeSeances = new ArrayList<>();
        initialiserInterface();
        remplirComboBox();
        chargerSeancesBDD();
        mettreAJourTable();
    }

    private void initialiserInterface() {
        setTitle("Gestion des Séances");
        setSize(1000, 600);
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
        btnRechercher.addActionListener(e -> rechercherSeance());
        panelRecherche.add(btnRechercher);

        btnAfficherTous = creerBoutonModerne("Afficher tous", new Color(103, 58, 183), Color.WHITE);
        btnAfficherTous.addActionListener(e -> mettreAJourTable());
        panelRecherche.add(btnAfficherTous);

        add(panelRecherche, BorderLayout.NORTH);

        // Table
        String[] colonnes = {"ID", "Moniteur", "Élève", "Véhicule", "Type", "Date", "Heure"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableSeance = new JTable(modelTable);
        tableSeance.setRowHeight(30);
        tableSeance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tableSeance);
        add(scroll, BorderLayout.CENTER);

        tableSeance.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) chargerSeanceSelectionnee();
        });

        tableSeance.getColumnModel().getColumn(0).setMinWidth(0);
        tableSeance.getColumnModel().getColumn(0).setMaxWidth(0);
        tableSeance.getColumnModel().getColumn(0).setWidth(0);

        // Formulaire
        JPanel panelFormulaire = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); gbc.anchor = GridBagConstraints.WEST;

        cbMoniteur = new JComboBox<>();
        cbEleve = new JComboBox<>();
        cbVehicule = new JComboBox<>();
        cbTypeCours = new JComboBox<>(new String[]{"pratique", "théorique"});
        cbTypeCours.addActionListener(e -> {
            // Désactiver véhicule si théorique
            if(cbTypeCours.getSelectedItem().toString().equals("théorique")) {
                cbVehicule.setEnabled(false);
            } else {
                cbVehicule.setEnabled(true);
            }
        });

        spDate = new JSpinner(new SpinnerDateModel());
        spDate.setEditor(new JSpinner.DateEditor(spDate, "yyyy-MM-dd"));

        spHeure = new JSpinner(new SpinnerDateModel());
        spHeure.setEditor(new JSpinner.DateEditor(spHeure, "HH:mm"));

        gbc.gridx=0; gbc.gridy=0; panelFormulaire.add(new JLabel("Moniteur:"), gbc);
        gbc.gridx=1; panelFormulaire.add(cbMoniteur, gbc);

        gbc.gridx=0; gbc.gridy=1; panelFormulaire.add(new JLabel("Élève:"), gbc);
        gbc.gridx=1; panelFormulaire.add(cbEleve, gbc);

        gbc.gridx=0; gbc.gridy=2; panelFormulaire.add(new JLabel("Véhicule:"), gbc);
        gbc.gridx=1; panelFormulaire.add(cbVehicule, gbc);

        gbc.gridx=0; gbc.gridy=3; panelFormulaire.add(new JLabel("Type de séance:"), gbc);
        gbc.gridx=1; panelFormulaire.add(cbTypeCours, gbc);

        gbc.gridx=0; gbc.gridy=4; panelFormulaire.add(new JLabel("Date:"), gbc);
        gbc.gridx=1; panelFormulaire.add(spDate, gbc);

        gbc.gridx=0; gbc.gridy=5; panelFormulaire.add(new JLabel("Heure:"), gbc);
        gbc.gridx=1; panelFormulaire.add(spHeure, gbc);

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        btnAjouter = creerBoutonModerne("Ajouter", new Color(76,175,80), Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterSeance());

        btnModifier = creerBoutonModerne("Modifier", new Color(63,81,181), Color.WHITE);
        btnModifier.addActionListener(e -> modifierSeance());

        btnSupprimer = creerBoutonModerne("Supprimer", new Color(244,67,54), Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerSeance());

        btnReinitialiser = creerBoutonModerne("Réinitialiser", new Color(158,158,158), Color.WHITE);
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());

        panelBoutons.add(btnAjouter); panelBoutons.add(btnModifier); panelBoutons.add(btnSupprimer); panelBoutons.add(btnReinitialiser);

        gbc.gridx=0; gbc.gridy=6; gbc.gridwidth=2; panelFormulaire.add(panelBoutons, gbc);

        labelStatut = new JLabel("Total: 0 séance(s)");
        gbc.gridy=7; panelFormulaire.add(labelStatut, gbc);

        add(panelFormulaire, BorderLayout.SOUTH);
    }

    private JButton creerBoutonModerne(String texte, Color bg, Color fg){
        JButton b = new JButton(texte); b.setBackground(bg); b.setForeground(fg); b.setFocusPainted(false);
        return b;
    }

    private void remplirComboBox() {
        try(Connection con = Connexion.getConnexion();
            Statement st = con.createStatement()) {

            ResultSet rsMon = st.executeQuery("SELECT id, nom, prenom FROM moniteur");
            while(rsMon.next()) cbMoniteur.addItem(new MoniteurItem(rsMon.getInt("id"), rsMon.getString("nom")+" "+rsMon.getString("prenom")));

            ResultSet rsEleve = st.executeQuery("SELECT idEleve, nom, prenom FROM eleve");
            while(rsEleve.next()) cbEleve.addItem(new EleveItem(rsEleve.getInt("idEleve"), rsEleve.getString("nom")+" "+rsEleve.getString("prenom")));

            ResultSet rsVeh = st.executeQuery("SELECT id, marque, modele FROM vehicule");
            while(rsVeh.next()) cbVehicule.addItem(new VehiculeItem(rsVeh.getInt("id"), rsVeh.getString("marque")+" "+rsVeh.getString("modele")));

        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Erreur lors de la récupération des données");
        }
    }

    private void chargerSeancesBDD() {
        listeSeances.clear();
        try(Connection con = Connexion.getConnexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT s.id, s.id_moniteur, m.nom as nomMon, m.prenom as prenomMon, " +
                "s.id_eleve, e.nom as nomEle, e.prenom as prenomEle, " +
                "s.id_vehicule, v.marque as marqueVeh, v.modele as modeleVeh, " +
                "s.date_cours, s.type_cours FROM seance s " +
                "JOIN moniteur m ON s.id_moniteur=m.id " +
                "JOIN eleve e ON s.id_eleve=e.idEleve " +
                "LEFT JOIN vehicule v ON s.id_vehicule=v.id")) {

            while(rs.next()){
                int id = rs.getInt("id");
                MoniteurItem mon = new MoniteurItem(rs.getInt("id_moniteur"), rs.getString("nomMon")+" "+rs.getString("prenomMon"));
                EleveItem ele = new EleveItem(rs.getInt("id_eleve"), rs.getString("nomEle")+" "+rs.getString("prenomEle"));
                String vehNom = rs.getString("marqueVeh") != null ? rs.getString("marqueVeh")+" "+rs.getString("modeleVeh") : "";
                VehiculeItem veh = new VehiculeItem(rs.getInt("id_vehicule"), vehNom);
                String date = rs.getString("date_cours");
                String type = rs.getString("type_cours");
                String heure = "08:00"; // par défaut
                Seance s = new Seance(id, mon, ele, veh, type, date, heure);
                listeSeances.add(s);
            }

        } catch(SQLException ex){ ex.printStackTrace(); }
    }

    private void ajouterSeance() {
        MoniteurItem mon = (MoniteurItem)cbMoniteur.getSelectedItem();
        EleveItem ele = (EleveItem)cbEleve.getSelectedItem();
        VehiculeItem veh = (VehiculeItem)cbVehicule.getSelectedItem();
        String typeUI = cbTypeCours.getSelectedItem().toString();
        String typeBDD = typeUI.equals("théorique") ? "theorie" : "pratique";
        String date = new SimpleDateFormat("yyyy-MM-dd").format(spDate.getValue());
        String heure = new SimpleDateFormat("HH:mm").format(spHeure.getValue());

        try(Connection con = Connexion.getConnexion();
            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO seance (id_moniteur,id_eleve,id_vehicule,type_cours,date_cours,duree) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, mon.getId());
            pst.setInt(2, ele.getId());
            if(typeBDD.equals("pratique")) pst.setInt(3, veh.getId()); else pst.setNull(3, Types.INTEGER);
            pst.setString(4, typeBDD);
            pst.setString(5, date);
            pst.setInt(6, 1); // durée par défaut
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                Seance s = new Seance(id, mon, ele, veh, typeUI, date, heure);
                listeSeances.add(s);
            }

        } catch(SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Erreur lors de l'ajout en BDD"); }

        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void modifierSeance() {
        int ligne = tableSeance.getSelectedRow();
        if(ligne==-1) return;
        Seance s = listeSeances.get(ligne);

        MoniteurItem mon = (MoniteurItem)cbMoniteur.getSelectedItem();
        EleveItem ele = (EleveItem)cbEleve.getSelectedItem();
        VehiculeItem veh = (VehiculeItem)cbVehicule.getSelectedItem();
        String typeUI = cbTypeCours.getSelectedItem().toString();
        String typeBDD = typeUI.equals("théorique") ? "theorie" : "pratique";
        String date = new SimpleDateFormat("yyyy-MM-dd").format(spDate.getValue());
        String heure = new SimpleDateFormat("HH:mm").format(spHeure.getValue());

        try(Connection con = Connexion.getConnexion();
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE seance SET id_moniteur=?,id_eleve=?,id_vehicule=?,type_cours=?,date_cours=? WHERE id=?")) {

            pst.setInt(1, mon.getId());
            pst.setInt(2, ele.getId());
            if(typeBDD.equals("pratique")) pst.setInt(3, veh.getId()); else pst.setNull(3, Types.INTEGER);
            pst.setString(4, typeBDD);
            pst.setString(5, date);
            pst.setInt(6, s.getId());
            pst.executeUpdate();

            s.setMoniteur(mon);
            s.setEleve(ele);
            s.setVehicule(veh);
            s.setDate(date);
            s.setHeure(heure);
            s.setType(typeUI);

        } catch(SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Erreur lors de la modification en BDD"); }

        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void supprimerSeance() {
        int ligne = tableSeance.getSelectedRow();
        if(ligne==-1) return;
        Seance s = listeSeances.get(ligne);

        try(Connection con = Connexion.getConnexion();
            PreparedStatement pst = con.prepareStatement("DELETE FROM seance WHERE id=?")) {

            pst.setInt(1, s.getId());
            pst.executeUpdate();
            listeSeances.remove(s);

        } catch(SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Erreur lors de la suppression en BDD"); }

        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void reinitialiserFormulaire() {
        cbMoniteur.setSelectedIndex(0);
        cbEleve.setSelectedIndex(0);
        cbVehicule.setSelectedIndex(0);
        cbTypeCours.setSelectedIndex(0);
        spDate.setValue(new Date());
        spHeure.setValue(new Date());
        tableSeance.clearSelection();
        cbVehicule.setEnabled(true);
    }

    private void mettreAJourTable() {
        modelTable.setRowCount(0);
        for(Seance s: listeSeances){
            modelTable.addRow(new Object[]{
                    s.getId(), s.getMoniteur().getNom(), s.getEleve().getNom(),
                    s.getType().equals("théorique") ? "" : s.getVehicule().getNom(),
                    s.getType(), s.getDate(), s.getHeure()});
        }
        labelStatut.setText("Total: "+listeSeances.size()+" séance(s)");
    }

    private void chargerSeanceSelectionnee() {
        int ligne = tableSeance.getSelectedRow();
        if(ligne==-1) return;
        Seance s = listeSeances.get(ligne);
        cbMoniteur.setSelectedItem(s.getMoniteur());
        cbEleve.setSelectedItem(s.getEleve());
        cbVehicule.setSelectedItem(s.getVehicule());
        cbTypeCours.setSelectedItem(s.getType());
        cbVehicule.setEnabled(!s.getType().equals("théorique"));
        try{
            spDate.setValue(new SimpleDateFormat("yyyy-MM-dd").parse(s.getDate()));
            spHeure.setValue(new SimpleDateFormat("HH:mm").parse(s.getHeure()));
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    private void rechercherSeance() {
        String texte = txtRecherche.getText().toLowerCase();
        if(texte.isEmpty()){ mettreAJourTable(); return; }
        List<Seance> res = new ArrayList<>();
        for(Seance s: listeSeances){
            if(s.getMoniteur().getNom().toLowerCase().contains(texte) ||
               s.getEleve().getNom().toLowerCase().contains(texte) ||
               s.getVehicule().getNom().toLowerCase().contains(texte) ||
               s.getDate().contains(texte) ||
               s.getHeure().contains(texte) ||
               s.getType().toLowerCase().contains(texte)){
                res.add(s);
            }
        }
        modelTable.setRowCount(0);
        for(Seance s: res){
            modelTable.addRow(new Object[]{s.getId(), s.getMoniteur().getNom(), s.getEleve().getNom(),
                    s.getType().equals("théorique") ? "" : s.getVehicule().getNom(),
                    s.getType(), s.getDate(), s.getHeure()});
        }
        labelStatut.setText("Total: "+res.size()+" séance(s)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrameSeance().setVisible(true));
    }

    // Classes auxiliaires
    static class MoniteurItem { int id; String nom; MoniteurItem(int i, String n){id=i;nom=n;} public String toString(){return nom;} public int getId(){return id;} public String getNom(){return nom;} }
    static class EleveItem { int id; String nom; EleveItem(int i, String n){id=i;nom=n;} public String toString(){return nom;} public int getId(){return id;} public String getNom(){return nom;} }
    static class VehiculeItem { int id; String nom; VehiculeItem(int i, String n){id=i;nom=n;} public String toString(){return nom;} public int getId(){return id;} public String getNom(){return nom;} }

    static class Seance {
        private int id;
        private MoniteurItem moniteur;
        private EleveItem eleve;
        private VehiculeItem vehicule;
        private String type;
        private String date, heure;

        public Seance(int id, MoniteurItem mon, EleveItem ele, VehiculeItem veh, String type, String d, String h){
            this.id=id; this.moniteur=mon; this.eleve=ele; this.vehicule=veh; this.type=type; this.date=d; this.heure=h;
        }

        public int getId(){return id;}
        public MoniteurItem getMoniteur(){return moniteur;}
        public EleveItem getEleve(){return eleve;}
        public VehiculeItem getVehicule(){return vehicule;}
        public String getDate(){return date;}
        public String getHeure(){return heure;}
        public String getType(){return type;}

        public void setMoniteur(MoniteurItem m){moniteur=m;}
        public void setEleve(EleveItem e){eleve=e;}
        public void setVehicule(VehiculeItem v){vehicule=v;}
        public void setDate(String d){date=d;}
        public void setHeure(String h){heure=h;}
        public void setType(String t){type=t;}
    }
}
