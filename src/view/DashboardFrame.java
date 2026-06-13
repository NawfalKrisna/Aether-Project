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
    private JLabel title;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private HomePanel homePanel;
    private ExportPdfPanel exportPdfPanel;

    // Menyimpan semua tombol menu sidebar agar mudah di-reset warnanya
    private final List<JButton> menuButtons = new ArrayList<>();

    // Warna utama sidebar dan status tombol
    private static final Color SIDEBAR_BG = new Color(15, 23, 42); // #0F172A – default
    // Warna saat kursor mouse berada di atas tombol
    private static final Color HOVER_BG = new Color(30, 41, 59); // #1E293B – on hover
    // Warna tombol yang sedang aktif/dipilih
    private static final Color ACTIVE_BG = new Color(37, 99, 235); // #2563EB – selected
    // Warna teks default
    private static final Color DEFAULT_FG = Color.WHITE;
    // Warna teks default
    private static final Color ACTIVE_FG = Color.WHITE;

    // Konstruktor utama DashboardFrame
    // Berfungsi mengatur properti awal frame dan memanggil tampilan utama
    public DashboardFrame() {
        setTitle("Aether Project - Aplikasi Berkas Surat");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initUI();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Yakin mau keluar aplikasi?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    // Method untuk membangun seluruh tampilan aplikasi
    // Meliputi header, sidebar, dan area konten utama
    private void initUI() {
        setLayout(new BorderLayout());

        // Membuat panel header di bagian atas aplikasi
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 23, 42));
        header.setPreferredSize(new Dimension(getWidth(), 50));
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Label judul aplikasi yang ditampilkan pada header
        title = new JLabel("AETHER PROJECT");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Tombol keluar aplikasi yang berada di pojok kanan atas header
        JButton btnExit = new JButton("X");
        btnExit.setForeground(Color.RED);
        btnExit.setFocusPainted(false);
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        // Mengubah tampilan tombol saat mouse diarahkan ke tombol keluar
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExit.setOpaque(true);
                btnExit.setBackground(Color.RED);
                btnExit.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                btnExit.setOpaque(false);
                btnExit.setBackground(null);
                btnExit.setForeground(Color.RED);
            }
        });
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin keluar aplikasi?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        header.add(title, BorderLayout.WEST);
        header.add(btnExit, BorderLayout.EAST);

        // Tambahin ke frame
        add(header, BorderLayout.NORTH);

        // Sidebar Panel
        // Membuat panel sidebar sebagai menu navigasi aplikasi
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42)); // Dark blue background (#0F172A)
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo Area
        // Label logo atau nama aplikasi pada sidebar
        JLabel logoLabel = new JLabel("AETHER PROJECT");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sidebar.add(logoLabel);

        // CardLayout digunakan untuk berpindah antar halaman tanpa membuka window baru
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(248, 250, 252)); // Light background (#F8FAFC)

        // Membuat objek halaman Dashboard
        homePanel = new HomePanel();
        // Membuat objek halaman Surat Masuk
        SuratMasukPanel suratMasukPanel = new SuratMasukPanel();
        // Membuat objek halaman Surat Keluar
        SuratKeluarPanel suratKeluarPanel = new SuratKeluarPanel();
        // Membuat objek halaman Export PDF
        exportPdfPanel = new ExportPdfPanel();
        // Membuat objek halaman About
        AboutPanel aboutPanel = new AboutPanel();

        // Add Panels to CardLayout
        mainContentPanel.add(homePanel, "Dashboard");
        mainContentPanel.add(suratMasukPanel, "SuratMasuk");
        mainContentPanel.add(suratKeluarPanel, "SuratKeluar");
        mainContentPanel.add(exportPdfPanel, "ExportPDF");
        mainContentPanel.add(aboutPanel, "About");

        // Menu Buttons
        // Menambahkan tombol menu ke sidebar beserta tujuan panel yang ditampilkan saat tombol diklik
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
 * Menampilkan kembali halaman Dashboard
 * serta memperbarui data yang ditampilkan.
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
 * Menampilkan halaman Export PDF
 * dan mengirimkan data tabel yang akan diekspor.
 *
 * @param columns Nama kolom tabel
 * @param data Data isi tabel
 * @param docTitle Judul dokumen PDF
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

    // Memperbarui data yang ada pada halaman Dashboard
    public void refreshHome() {
        if (homePanel != null) {
            homePanel.refreshData();
        }
    }

   /**
 * Mengembalikan seluruh tombol menu ke tampilan normal
 * sebelum salah satu tombol diaktifkan.
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


       /**
 * Membuat tombol menu sidebar beserta fungsi navigasinya.
 *
 * @param sidebar Panel sidebar tempat tombol ditambahkan
 * @param text Teks yang ditampilkan pada tombol
 * @param cardName Nama panel tujuan pada CardLayout
 */
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

        // Menangani aksi saat tombol menu diklik
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengembalikan semua tombol ke kondisi tidak aktif
                resetAllMenuButtons();

                // Menandai tombol yang dipilih sebagai tombol aktif
                btn.setBackground(ACTIVE_BG);
                btn.setForeground(ACTIVE_FG);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

                // Menampilkan halaman yang dipilih pada area konten
                if ("Dashboard".equals(cardName)) {
                    homePanel.refreshData();
                }
                cardLayout.show(mainContentPanel, cardName);
                setPageTitle(text);
            }
        });

        // Keep track of this button
        menuButtons.add(btn);

        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }
// Mengubah judul halaman pada bagian header sesuai dengan menu yang dipilih
    public void setPageTitle(String text) {
        title.setText(text);
    }
}
