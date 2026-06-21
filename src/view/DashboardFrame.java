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
    private boolean isMaximized = true;
    private Rectangle normalBounds;

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
        normalBounds = getBounds();
        isMaximized = true;
        setMinimumSize(new Dimension(900, 600));
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

        // Sidebar Panel (Deklarasi di awal agar bisa diakses oleh tombol toggle)
        JPanel sidebar = new JPanel();

        // Membuat panel header di bagian atas aplikasi
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(2, 6, 23)); // lebih gelap
        header.setPreferredSize(new Dimension(getWidth(), 50));
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Tombol Toggle Sidebar di dalam Sidebar
        JButton btnToggleSidebar = new JButton(new MenuIcon("burgerbar"));
        btnToggleSidebar.setToolTipText("Buka/Tutup Sidebar");
        btnToggleSidebar.setForeground(Color.WHITE);
        btnToggleSidebar.setBackground(new Color(15, 23, 42)); // Sesuai warna sidebar
        btnToggleSidebar.setFocusPainted(false);
        btnToggleSidebar.setBorderPainted(false);
        btnToggleSidebar.setOpaque(true);
        btnToggleSidebar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnToggleSidebar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Buat agar memenuhi lebar penuh sidebar
        btnToggleSidebar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnToggleSidebar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnToggleSidebar.setHorizontalAlignment(SwingConstants.LEFT);
        btnToggleSidebar.setMargin(new Insets(0, 15, 0, 0)); // Margin kiri 15px

        btnToggleSidebar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnToggleSidebar.setBackground(new Color(30, 41, 59));
            }

            public void mouseExited(MouseEvent e) {
                btnToggleSidebar.setBackground(new Color(15, 23, 42));
            }
        });

        // Logika Animasi Slide (Mini Sidebar)
        final int TARGET_WIDTH = 250;
        final int COLLAPSED_WIDTH = 45;
        final int[] currentWidth = { TARGET_WIDTH };
        final boolean[] isSidebarOpen = { true };
        final Timer[] slideTimer = { null };

        ActionListener toggleAction = e -> {
            if (slideTimer[0] != null && slideTimer[0].isRunning()) {
                return;
            }
            if (isSidebarOpen[0]) {
                // Sembunyikan label (misal Admin User)
                for (Component c : sidebar.getComponents()) {
                    if (c instanceof JLabel) {
                        c.setVisible(false);
                    }
                }

                // Sembunyikan teks, biarkan hanya icon yang tampil
                for (JButton btn : menuButtons) {
                    btn.setText("");
                    btn.setHorizontalAlignment(SwingConstants.CENTER);
                    btn.setMargin(new Insets(0, 0, 0, 0));
                }
                btnToggleSidebar.setHorizontalAlignment(SwingConstants.CENTER);
                btnToggleSidebar.setMargin(new Insets(0, 0, 0, 0));

                // Proses Tutup (Slide ke kiri sampai 45px)
                slideTimer[0] = new Timer(10, ev -> {
                    currentWidth[0] -= 25;
                    if (currentWidth[0] <= COLLAPSED_WIDTH) {
                        currentWidth[0] = COLLAPSED_WIDTH;
                        slideTimer[0].stop();
                        isSidebarOpen[0] = false;
                    }
                    Dimension d = new Dimension(currentWidth[0], sidebar.getHeight() > 0 ? sidebar.getHeight() : 600);
                    sidebar.setPreferredSize(d);
                    sidebar.setMinimumSize(d);
                    sidebar.setMaximumSize(d);
                    sidebar.revalidate();
                    getContentPane().validate();
                    repaint();
                });
                slideTimer[0].start();
            } else {
                // Proses Buka (Slide ke kanan)
                slideTimer[0] = new Timer(10, ev -> {
                    currentWidth[0] += 25;
                    if (currentWidth[0] >= TARGET_WIDTH) {
                        currentWidth[0] = TARGET_WIDTH;
                        slideTimer[0].stop();
                        isSidebarOpen[0] = true;

                        // Tampilkan kembali label
                        for (Component c : sidebar.getComponents()) {
                            if (c instanceof JLabel) {
                                c.setVisible(true);
                            }
                        }

                        // Kembalikan teks tombol dan posisikan di kiri
                        for (JButton btn : menuButtons) {
                            btn.setText((String) btn.getClientProperty("fullText"));
                            btn.setHorizontalAlignment(SwingConstants.LEFT);
                            btn.setMargin(new Insets(0, 15, 0, 0));
                        }
                        btnToggleSidebar.setHorizontalAlignment(SwingConstants.LEFT);
                        btnToggleSidebar.setMargin(new Insets(0, 15, 0, 0));
                    }
                    Dimension d = new Dimension(currentWidth[0], sidebar.getHeight() > 0 ? sidebar.getHeight() : 600);
                    sidebar.setPreferredSize(d);
                    sidebar.setMinimumSize(d);
                    sidebar.setMaximumSize(d);
                    sidebar.revalidate();
                    getContentPane().validate();
                    repaint();
                });
                slideTimer[0].start();
            }
        };

        btnToggleSidebar.addActionListener(toggleAction);

        // Label judul aplikasi yang ditampilkan pada header
        title = new JLabel("AETHER PROJECT");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel titlePanel = new JPanel(new BorderLayout(15, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);

        // Tombol keluar aplikasi yang berada di pojok kanan atas header
        JButton btnExit = new JButton("X");
        btnExit.setForeground(Color.WHITE);
        btnExit.setBackground(new Color(220, 38, 38)); // merah
        btnExit.setFocusPainted(false);
        btnExit.setBorderPainted(false);
        btnExit.setOpaque(true);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setPreferredSize(new Dimension(45, 30));
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setMargin(new Insets(0, 0, 0, 0));

        // Hover effect
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExit.setBackground(new Color(239, 68, 68)); // lebih terang
            }

            public void mouseExited(MouseEvent e) {
                btnExit.setBackground(new Color(220, 38, 38));
            }
        });
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

        // MINIMIZE
        JButton btnMinimize = new JButton("_");
        btnMinimize.setForeground(Color.WHITE);
        btnMinimize.setBackground(new Color(30, 41, 59));
        btnMinimize.setFocusPainted(false);
        btnMinimize.setBorderPainted(false);
        btnMinimize.setOpaque(true);
        btnMinimize.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMinimize.setPreferredSize(new Dimension(45, 30));
        btnMinimize.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMinimize.setMargin(new Insets(0, 0, 0, 0));

        btnMinimize.addActionListener(e -> {
            setExtendedState(JFrame.ICONIFIED);
        });

        btnMinimize.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnMinimize.setBackground(new Color(51, 65, 85));
            }

            public void mouseExited(MouseEvent e) {
                btnMinimize.setBackground(new Color(30, 41, 59));
            }
        });

        // MAXIMIZE
        JButton btnMaximize = new JButton("\u25A1");
        btnMaximize.setForeground(Color.WHITE);
        btnMaximize.setBackground(new Color(30, 41, 59));
        btnMaximize.setFocusPainted(false);
        btnMaximize.setBorderPainted(false);
        btnMaximize.setOpaque(true);
        btnMaximize.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMaximize.setPreferredSize(new Dimension(45, 30));
        btnMaximize.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMaximize.setMargin(new Insets(0, 0, 0, 0));

        btnMaximize.addActionListener(e -> {
            if (isMaximized) {
                // balik ke ukuran sebelumnya
                setSize(normalBounds.width, normalBounds.height);
                setLocationRelativeTo(null); // INI YANG BIKIN CENTER
                isMaximized = false;
            } else {
                // simpan ukuran sebelum maximize
                normalBounds = getBounds();
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                isMaximized = true;
            }
        });

        btnMaximize.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnMaximize.setBackground(new Color(51, 65, 85));
            }

            public void mouseExited(MouseEvent e) {
                btnMaximize.setBackground(new Color(30, 41, 59));
            }
        });

        header.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    btnMaximize.doClick();
                }
            }
        });

        header.add(titlePanel, BorderLayout.WEST);
        final Point[] mouseDownCompCoords = { null };

        header.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }
        });

        header.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(
                        currCoords.x - mouseDownCompCoords[0].x,
                        currCoords.y - mouseDownCompCoords[0].y);
            }
        });
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        controlPanel.setOpaque(false);

        controlPanel.add(btnMinimize);
        controlPanel.add(btnMaximize);
        controlPanel.add(btnExit);

        header.add(controlPanel, BorderLayout.EAST);

        // Tambahin ke frame
        add(header, BorderLayout.NORTH);

        // Sidebar Panel
        // Membuat panel sidebar sebagai menu navigasi aplikasi
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42)); // Dark blue background (#0F172A)
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // Dihilangkan border kiri-kanan agar tombol
                                                                          // bisa full width

        // Tambahkan tombol toggle langsung ke sidebar agar ukurannya stretch full width
        sidebar.add(btnToggleSidebar);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing bawah toggle

        // Logo dihapus karena sudah ada di header

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

        // Menu Buttons dengan Custom Vector Icons
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
            if ("Dashboard".equals(btn.getClientProperty("fullText"))) {
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
     * @param columns  Nama kolom tabel
     * @param data     Data isi tabel
     * @param docTitle Judul dokumen PDF
     */

    public void showExportPdf(String[] columns, java.util.List<Object[]> data, String docTitle) {
        exportPdfPanel.loadData(columns, data, docTitle);
        // Activate the Export PDF menu button styling
        resetAllMenuButtons();
        // Find and highlight the Export PDF button
        for (JButton btn : menuButtons) {
            if ("Export PDF".equals(btn.getClientProperty("fullText"))) {
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
        btn.setIcon(new MenuIcon(text));
        btn.setIconTextGap(15);
        btn.putClientProperty("fullText", text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(DEFAULT_FG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 15, 0, 0)); // Match margin toggle sidebar

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

    // Custom Icon untuk Sidebar agar cross-platform dan ukurannya presisi
    private static class MenuIcon implements Icon {
        private String type;

        public MenuIcon(String type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            if (type.equals("Hamburger")) {
                g2.drawLine(x + 2, y + 4, x + 16, y + 4);
                g2.drawLine(x + 2, y + 9, x + 16, y + 9);
                g2.drawLine(x + 2, y + 14, x + 16, y + 14);
            } else if (type.equals("Dashboard")) {
                int[] px = { x + 9, x + 16, x + 2 };
                int[] py = { y + 2, y + 8, y + 8 };
                g2.drawPolygon(px, py, 3);
                g2.drawRect(x + 4, y + 8, 10, 8);
            } else if (type.equals("Surat Masuk")) {
                g2.drawRect(x + 3, y + 2, 12, 12);
                g2.drawLine(x + 9, y + 4, x + 9, y + 10);
                g2.drawLine(x + 6, y + 7, x + 9, y + 10);
                g2.drawLine(x + 12, y + 7, x + 9, y + 10);
            } else if (type.equals("Surat Keluar")) {
                g2.drawRect(x + 3, y + 2, 12, 12);
                g2.drawLine(x + 9, y + 10, x + 9, y + 4);
                g2.drawLine(x + 6, y + 7, x + 9, y + 4);
                g2.drawLine(x + 12, y + 7, x + 9, y + 4);
            } else if (type.equals("Arsip Semua Surat")) {
                g2.drawRect(x + 2, y + 3, 14, 12);
                g2.drawLine(x + 2, y + 7, x + 16, y + 7);
            } else if (type.equals("Export PDF")) {
                g2.drawRect(x + 3, y + 2, 12, 14);
                g2.drawLine(x + 6, y + 6, x + 12, y + 6);
                g2.drawLine(x + 6, y + 9, x + 12, y + 9);
                g2.drawLine(x + 6, y + 12, x + 10, y + 12);
            } else if (type.equals("About")) {
                g2.drawOval(x + 2, y + 2, 14, 14);
                g2.drawLine(x + 9, y + 6, x + 9, y + 6); // dot
                g2.drawLine(x + 9, y + 8, x + 9, y + 13); // line
            }
            g2.dispose();
        }
    }
}
