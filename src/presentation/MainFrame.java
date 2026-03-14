package autoécole.présentation;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class MainFrame extends JFrame {

    private JPanel panelMenu;
    private static final Color COULEUR_PRINCIPALE = new Color(231, 76, 60);
    private static final Color COULEUR_PANEL = Color.WHITE;
    private static final Color COULEUR_FOND = new Color(245, 245, 245);
    private static final Color COULEUR_BORDURE = new Color(220, 220, 220);

    public MainFrame() {
        setTitle("Auto École - Accueil");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COULEUR_FOND);

        // Confirmation de fermeture
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Voulez-vous vraiment quitter l'application ?",
                        "Quitter",
                        JOptionPane.YES_NO_OPTION
                ) == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        // Panel principal avec BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COULEUR_FOND);
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        creerMenuLateral();

        // Panel central avec image qui remplit tout l'espace disponible
        JPanel panelPhoto = new JPanel() {
            private Image img;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img == null) {
                    URL imageUrl = getClass().getResource("auto_ecole.png");
                    if (imageUrl != null) {
                        ImageIcon icon = new ImageIcon(imageUrl);
                        img = icon.getImage();
                    }
                }
                if (img != null) {
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();
                    int imgWidth = img.getWidth(null);
                    int imgHeight = img.getHeight(null);

                    double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                    int newWidth = (int) (imgWidth * scale);
                    int newHeight = (int) (imgHeight * scale);

                    int x = (panelWidth - newWidth) / 2; // centré horizontalement
                    int y = (panelHeight - newHeight) / 2; // centré verticalement

                    g.drawImage(img, x, y, newWidth, newHeight, this);
                } else {
                    g.setColor(Color.RED);
                    g.drawString("Image non trouvée", getWidth() / 2 - 50, getHeight() / 2);
                }
            }
        };
        panelPhoto.setBackground(COULEUR_FOND);

        panelPrincipal.add(panelMenu, BorderLayout.WEST);
        panelPrincipal.add(panelPhoto, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void creerMenuLateral() {
        panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setPreferredSize(new Dimension(240, 0));
        panelMenu.setBackground(COULEUR_PANEL);
        panelMenu.setBorder(new CompoundBorder(
                new LineBorder(COULEUR_BORDURE, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel labelMenuTitre = new JLabel("Menu Principal");
        labelMenuTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelMenuTitre.setForeground(COULEUR_PRINCIPALE);
        labelMenuTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelMenuTitre.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelMenu.add(labelMenuTitre);

        panelMenu.add(creerBoutonMenu("Élèves", COULEUR_PRINCIPALE, () -> new FrameEleve().setVisible(true)));
        panelMenu.add(Box.createVerticalStrut(12));

        panelMenu.add(creerBoutonMenu("Moniteurs", COULEUR_PRINCIPALE, () -> new FrameMoniteur().setVisible(true)));
        panelMenu.add(Box.createVerticalStrut(12));

        panelMenu.add(creerBoutonMenu("Véhicules", COULEUR_PRINCIPALE, () -> new FrameVehicule().setVisible(true)));
        panelMenu.add(Box.createVerticalStrut(12));

        panelMenu.add(creerBoutonMenu("Paiements", COULEUR_PRINCIPALE, () -> new FramePaiement().setVisible(true)));
        panelMenu.add(Box.createVerticalStrut(12));

        panelMenu.add(creerBoutonMenu("Séances", COULEUR_PRINCIPALE, () -> new FrameSeance().setVisible(true)));
        panelMenu.add(Box.createVerticalStrut(12));

        panelMenu.add(creerBoutonMenu("Examens", COULEUR_PRINCIPALE, () -> new FrameExamen().setVisible(true)));
        panelMenu.add(Box.createVerticalStrut(20));

        panelMenu.add(creerBoutonMenu("Quitter", Color.GRAY, () ->
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        ));
    }

    private JButton creerBoutonMenu(String texte, Color couleur, Runnable action) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 45));

        btn.addActionListener(e -> action.run());

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleur.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleur);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleur.darker());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn.setBackground(couleur);
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
