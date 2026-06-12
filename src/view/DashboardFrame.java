package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private HomePanel homePanel;
    private ExportPdfPanel exportPdfPanel;

    // Track all sidebar menu buttons so we can reset their colors
    private final List<JButton> menuButtons = new ArrayList<>();

    // Sidebar color palette
    private static final Color SIDEBAR_BG = new Color(15, 23, 42); // #0F172A – default
    private static final Color HOVER_BG = new Color(30, 41, 59); // #1E293B – on hover
    private static final Color ACTIVE_BG = new Color(37, 99, 235); // #2563EB – selected
    private static final Color DEFAULT_FG = Color.WHITE;
    private static final Color ACTIVE_FG = Color.WHITE;

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
        homePanel = new HomePanel();
        SuratMasukPanel suratMasukPanel = new SuratMasukPanel();
        SuratKeluarPanel suratKeluarPanel = new SuratKeluarPanel();
        exportPdfPanel = new ExportPdfPanel();
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

    /**
     * Navigates back to the Dashboard panel and refreshes it.
     * Called from ExportPdfPanel's "Back" button.
     */
    public void showDashboard() {
        resetAllMenuButtons();
        for (JButton btn : menuButtons) {
            if ("Dashboard".equals(btn.getText())) {
                btn.setBackground(ACTIVE_BG);
                btn.setForeground(ACTIVE_FG);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                break;
            }
        }
        homePanel.refreshData();
        cardLayout.show(mainContentPanel, "Dashboard");
    }

    /**
     * Switches to ExportPDF panel, passing the current table data for preview and
     * download.
     * Called from SuratMasukPanel / SuratKeluarPanel when "Export ke PDF" is
     * clicked.
     *
     * @param columns  Column header names to display in preview
     * @param data     Row data (each Object[] is one row)
     * @param docTitle Document title used in the generated PDF (e.g. "Surat Masuk")
     */
    public void showExportPdf(String[] columns, java.util.List<Object[]> data, String docTitle) {
        exportPdfPanel.loadData(columns, data, docTitle);
        // Activate the Export PDF menu button styling
        resetAllMenuButtons();
        // Find and highlight the Export PDF button
        for (JButton btn : menuButtons) {
            if ("Export PDF".equals(btn.getText())) {
                btn.setBackground(ACTIVE_BG);
                btn.setForeground(ACTIVE_FG);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                break;
            }
        }
        cardLayout.show(mainContentPanel, "ExportPDF");
    }

    public void refreshHome() {
        if (homePanel != null) {
            homePanel.refreshData();
        }
    }

    /**
     * Reset every menu button back to its default (inactive) appearance.
     * Call this before activating the newly-selected button.
     */
    private void resetAllMenuButtons() {
        for (JButton btn : menuButtons) {
            btn.setBackground(SIDEBAR_BG);
            btn.setForeground(DEFAULT_FG);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setOpaque(true);
        }
    }

    private void addMenuButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(DEFAULT_FG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Hover State ---
        // Change background on mouse enter/exit, but only if the button
        // is NOT the currently-active one (active color takes priority).
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Only apply hover color when the button is not active
                if (!btn.getBackground().equals(ACTIVE_BG)) {
                    btn.setBackground(HOVER_BG);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Revert to default color when not active
                if (!btn.getBackground().equals(ACTIVE_BG)) {
                    btn.setBackground(SIDEBAR_BG);
                }
            }
        });

        // --- Active State (on click) ---
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Reset all buttons to default appearance
                resetAllMenuButtons();

                // 2. Mark the clicked button as active
                btn.setBackground(ACTIVE_BG);
                btn.setForeground(ACTIVE_FG);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

                // 3. Navigate to the selected panel
                if ("Dashboard".equals(cardName)) {
                    homePanel.refreshData();
                }
                cardLayout.show(mainContentPanel, cardName);
            }
        });

        // Keep track of this button
        menuButtons.add(btn);

        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}
