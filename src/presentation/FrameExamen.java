package autoécole.présentation;

import autoécole.dao.EleveDAO;
import autoécole.dao.ExamenDAO;
import autoécole.model.Eleve;
import autoécole.model.Examen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class FrameExamen extends JFrame {

    private JComboBox<String> comboEleves;
    private JComboBox<String> comboTypeExamen;
    private JComboBox<String> comboResultat;
    private JSpinner spinnerDate;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnReinitialiser;
    private JTable tableExamen;
    private DefaultTableModel modelTable;
    private List<Examen> listeExamens;
    private List<Eleve> listeEleves;

    public FrameExamen() {
        listeEleves = EleveDAO.liste();
        listeExamens = ExamenDAO.liste();
        initialiserInterface();
    }

    private void initialiserInterface() {
        setTitle("Gestion des Examens");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // --- Table ---
        modelTable = new DefaultTableModel(new String[]{"ID","Élève","Type","Résultat","Date"}, 0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        tableExamen = new JTable(modelTable);
        tableExamen.setRowHeight(30);
        tableExamen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableExamen.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) chargerExamenSelectionne();
        });

        // Masquer la colonne ID
        tableExamen.getColumnModel().getColumn(0).setMinWidth(0);
        tableExamen.getColumnModel().getColumn(0).setMaxWidth(0);
        tableExamen.getColumnModel().getColumn(0).setWidth(0);
        tableExamen.getColumnModel().getColumn(0).setPreferredWidth(0);

        add(new JScrollPane(tableExamen), BorderLayout.CENTER);

        // --- Formulaire ---
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        comboEleves = new JComboBox<>();
        remplirComboEleves();

        comboTypeExamen = new JComboBox<>(new String[]{"Code","Créno","Circulation"});
        comboResultat = new JComboBox<>(new String[]{"Réussi","Échoué"});

        spinnerDate = new JSpinner(new SpinnerDateModel());
        spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate,"yyyy-MM-dd"));

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Élève:"), gbc);
        gbc.gridx=1; form.add(comboEleves, gbc);

        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Type:"), gbc);
        gbc.gridx=1; form.add(comboTypeExamen, gbc);

        gbc.gridx=0; gbc.gridy=2; form.add(new JLabel("Résultat:"), gbc);
        gbc.gridx=1; form.add(comboResultat, gbc);

        gbc.gridx=0; gbc.gridy=3; form.add(new JLabel("Date:"), gbc);
        gbc.gridx=1; form.add(spinnerDate, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        btnAjouter = creerBouton("Ajouter", new Color(76,175,80), Color.WHITE);
        btnAjouter.addActionListener(e -> ajouterExamen());
        btnModifier = creerBouton("Modifier", new Color(63,81,181), Color.WHITE);
        btnModifier.addActionListener(e -> modifierExamen());
        btnSupprimer = creerBouton("Supprimer", new Color(244,67,54), Color.WHITE);
        btnSupprimer.addActionListener(e -> supprimerExamen());
        btnReinitialiser = creerBouton("Réinitialiser", new Color(158,158,158), Color.WHITE);
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());

        panelBoutons.add(btnAjouter); panelBoutons.add(btnModifier); panelBoutons.add(btnSupprimer); panelBoutons.add(btnReinitialiser);

        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2; form.add(panelBoutons, gbc);

        add(form, BorderLayout.SOUTH);

        mettreAJourTable();
    }

    private JButton creerBouton(String texte, Color bg, Color fg){
        JButton b = new JButton(texte);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        return b;
    }

    private void remplirComboEleves(){
        comboEleves.removeAllItems();
        for(Eleve e : listeEleves){
            comboEleves.addItem(e.getNom() + " " + e.getPrenom());
        }
    }

    private void ajouterExamen(){
        try {
            int idEleve = listeEleves.get(comboEleves.getSelectedIndex()).getIdEleve();
            String typeExamen = switch(comboTypeExamen.getSelectedIndex()) {
                case 0 -> "code";
                case 1 -> "creno";
                case 2 -> "circulation";
                default -> "";
            };
            String resultat = switch(comboResultat.getSelectedIndex()){
                case 0 -> "reussi";
                case 1 -> "echoue";
                default -> "";
            };
            Date d = (Date) spinnerDate.getValue();
            LocalDate dateExamen = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Examen e = new Examen();
            e.setIdEleve(idEleve);
            e.setTypeExamen(typeExamen);
            e.setResultat(resultat);
            e.setDateExamen(dateExamen);

            ExamenDAO.ajouter(e);
            listeExamens.add(e);

            mettreAJourTable();
            reinitialiserFormulaire();

        } catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Erreur: "+ex.getMessage());
        }
    }

    private void modifierExamen(){
        int ligne = tableExamen.getSelectedRow();
        if(ligne==-1) return;
        Examen e = listeExamens.get(ligne);
        try {
            int idEleve = listeEleves.get(comboEleves.getSelectedIndex()).getIdEleve();
            String typeExamen = switch(comboTypeExamen.getSelectedIndex()) {
                case 0 -> "code";
                case 1 -> "creno";
                case 2 -> "circulation";
                default -> "";
            };
            String resultat = switch(comboResultat.getSelectedIndex()){
                case 0 -> "reussi";
                case 1 -> "echoue";
                default -> "";
            };
            Date d = (Date) spinnerDate.getValue();
            LocalDate dateExamen = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            e.setIdEleve(idEleve);
            e.setTypeExamen(typeExamen);
            e.setResultat(resultat);
            e.setDateExamen(dateExamen);

            ExamenDAO.modifier(e);

            mettreAJourTable();
            reinitialiserFormulaire();

        } catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Erreur: "+ex.getMessage());
        }
    }

    private void supprimerExamen(){
        int ligne = tableExamen.getSelectedRow();
        if(ligne==-1) return;
        Examen e = listeExamens.get(ligne);
        ExamenDAO.supprimer(e.getIdExamen());
        listeExamens.remove(ligne);
        mettreAJourTable();
        reinitialiserFormulaire();
    }

    private void reinitialiserFormulaire(){
        if(comboEleves.getItemCount()>0) comboEleves.setSelectedIndex(0);
        comboTypeExamen.setSelectedIndex(0);
        comboResultat.setSelectedIndex(0);
        spinnerDate.setValue(new Date());
        tableExamen.clearSelection();
    }

    private void mettreAJourTable(){
        modelTable.setRowCount(0);
        for(Examen e : listeExamens){
            String nomEleve = "";
            for(Eleve el : listeEleves){
                if(el.getIdEleve() == e.getIdEleve()){
                    nomEleve = el.getNom() + " " + el.getPrenom();
                    break;
                }
            }
            String type = switch(e.getTypeExamen()){
                case "code" -> "Code";
                case "creno" -> "Créno";
                case "circulation" -> "Circulation";
                default -> "";
            };
            String res = e.getResultat().equals("reussi") ? "Réussi" : "Échoué";

            modelTable.addRow(new Object[]{
                e.getIdExamen(),  // reste caché
                nomEleve,
                type,
                res,
                e.getDateExamen()
            });
        }
    }

    private void chargerExamenSelectionne(){
        int ligne = tableExamen.getSelectedRow();
        if(ligne==-1) return;
        Examen e = listeExamens.get(ligne);

        for(int i=0;i<listeEleves.size();i++){
            if(listeEleves.get(i).getIdEleve() == e.getIdEleve()){
                comboEleves.setSelectedIndex(i);
                break;
            }
        }
        comboTypeExamen.setSelectedIndex(switch(e.getTypeExamen()){
            case "code" -> 0;
            case "creno" -> 1;
            case "circulation" -> 2;
            default -> 0;
        });
        comboResultat.setSelectedIndex(e.getResultat().equals("reussi") ? 0 : 1);
        spinnerDate.setValue(Date.from(e.getDateExamen().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new FrameExamen().setVisible(true));
    }
}
