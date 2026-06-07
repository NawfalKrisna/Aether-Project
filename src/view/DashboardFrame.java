package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public DashboardFrame() {
        setTitle("Aether Project - Aplikasi Berkas Surat");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center screen
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42)); // Dark blue background (#0F172A)
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo Area
        JLabel logoLabel = new JLabel("AETHER PROJECT");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sidebar.add(logoLabel);

        // Main Content Area with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(248, 250, 252)); // Light background (#F8FAFC)

        // Create Panels
        HomePanel homePanel = new HomePanel();
        SuratMasukPanel suratMasukPanel = new SuratMasukPanel();
        SuratKeluarPanel suratKeluarPanel = new SuratKeluarPanel();
        ExportPdfPanel exportPdfPanel = new ExportPdfPanel();
        AboutPanel aboutPanel = new AboutPanel();

        // Add Panels to CardLayout
        mainContentPanel.add(homePanel, "Dashboard");
        mainContentPanel.add(suratMasukPanel, "SuratMasuk");
        mainContentPanel.add(suratKeluarPanel, "SuratKeluar");
        mainContentPanel.add(exportPdfPanel, "ExportPDF");
        mainContentPanel.add(aboutPanel, "About");

        // Menu Buttons
        addMenuButton(sidebar, "Dashboard", "Dashboard");
        addMenuButton(sidebar, "Surat Masuk", "SuratMasuk");
        addMenuButton(sidebar, "Surat Keluar", "SuratKeluar");
        addMenuButton(sidebar, "Arsip Semua Surat", "SuratMasuk"); // Reuse for now
        addMenuButton(sidebar, "Export PDF", "ExportPDF");
        addMenuButton(sidebar, "About", "About");

        sidebar.add(Box.createVerticalGlue()); // Push everything up

        // User Info at bottom
        JLabel userLabel = new JLabel("Admin User");
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(userLabel);

        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void addMenuButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(15, 23, 42));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainContentPanel, cardName);
            }
        });
        
        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}
