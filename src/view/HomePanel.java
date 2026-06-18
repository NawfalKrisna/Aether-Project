package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import dao.SuratKeluarDAO;
import dao.SuratMasukDAO;
import model.SuratMasuk;
import model.SuratKeluar;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.text.SimpleDateFormat;

import java.awt.*;

public class HomePanel extends JPanel {

    private JLabel totalMasukLabel;
    private JLabel totalKeluarLabel;
    private JLabel totalSuratLabel;
    private JLabel suratHariIniLabel;
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;
    private JComboBox<String> filterJenisCombo;

    public HomePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        initUI();
        loadDashboardData();
    }

    private void initUI() {
        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Selamat Datang, Admin User!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel subtitleLabel = new JLabel("Berikut ringkasan berkas surat pada sistem.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        headerPanel.add(welcomeLabel);
        headerPanel.add(subtitleLabel);

        // Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        totalMasukLabel = new JLabel();
        totalKeluarLabel = new JLabel();
        totalSuratLabel = new JLabel();
        suratHariIniLabel = new JLabel();

        cardsPanel.add(createCard("Surat Masuk", totalMasukLabel, new Color(220, 252, 231), new Color(22, 163, 74)));
        cardsPanel.add(createCard("Surat Keluar", totalKeluarLabel, new Color(224, 242, 254), new Color(2, 132, 199)));
        cardsPanel.add(createCard("Total Surat", totalSuratLabel, new Color(243, 232, 255), new Color(147, 51, 234)));
        cardsPanel
                .add(createCard("Surat Hari Ini", suratHariIniLabel, new Color(255, 237, 213), new Color(234, 88, 12)));

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(cardsPanel, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // Recent Table
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel tableTitle = new JLabel("Surat Terbaru");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Search Bar for dashboard table
        JPanel tableHeaderPanel = new JPanel(new BorderLayout(12, 0));
        tableHeaderPanel.setOpaque(false);
        tableHeaderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        searchField.setToolTipText("Cari surat terbaru...");
        searchField.setPreferredSize(new Dimension(220, 30));

        JLabel searchIcon = new JLabel("\uD83D\uDD0D  Cari:");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchIcon.setForeground(new Color(100, 116, 139));

        // Sort by Date combo box
        JLabel sortLabel = new JLabel("Urutkan Tanggal:");
        sortLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sortLabel.setForeground(new Color(100, 116, 139));

        JComboBox<String> sortDateCombo = new JComboBox<>(new String[] {
                "Terbaru ke Terlama",
                "Terlama ke Terbaru"
        });
        sortDateCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sortDateCombo.setPreferredSize(new Dimension(180, 30));

        // Filter Jenis Surat combo box
        JLabel filterLabel = new JLabel("Jenis Surat:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterLabel.setForeground(new Color(100, 116, 139));

        filterJenisCombo = new JComboBox<>(new String[] {
                "Semua Jenis",
                "Surat Masuk",
                "Surat Keluar"
        });
        filterJenisCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterJenisCombo.setPreferredSize(new Dimension(140, 30));

        JPanel searchRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchRight.setOpaque(false);
        searchRight.add(sortLabel);
        searchRight.add(sortDateCombo);
        searchRight.add(filterLabel);
        searchRight.add(filterJenisCombo);
        searchRight.add(searchIcon);
        searchRight.add(searchField);

        tableHeaderPanel.add(tableTitle, BorderLayout.WEST);
        tableHeaderPanel.add(searchRight, BorderLayout.EAST);

        tableContainer.add(tableHeaderPanel, BorderLayout.NORTH);

        String[] columns = {
                "No",
                "Nomor Surat",
                "Jenis Surat",
                "Tanggal",
                "Perihal",
                "Aksi"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // only Aksi column
            }
        };
        rowSorter = new TableRowSorter<>(model);

        // Custom comparator: parse "dd/MM/yyyy" strings into real Date objects
        // so sorting is chronological, NOT alphabetical.
        // Date is at column index 3 in this dashboard table.
        rowSorter.setComparator(3, new Comparator<String>() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public int compare(String s1, String s2) {
                try {
                    java.util.Date d1 = sdf.parse(s1);
                    java.util.Date d2 = sdf.parse(s2);
                    return d1.compareTo(d2);
                } catch (Exception e) {
                    return 0;
                }
            }
        });

        table = new JTable(model);
        table.setRowSorter(rowSorter);
        for (java.awt.event.MouseListener ml : table.getTableHeader().getMouseListeners()) {
            table.getTableHeader().removeMouseListener(ml);
        }
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));

        // Wire up search + filter combo to the shared applyFilters method
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }
        });

        filterJenisCombo.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                applyFilters();
            }
        });

        // Sort combo box listener – apply sort order on the date column (index 3)
        sortDateCombo.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String selected = (String) sortDateCombo.getSelectedItem();
                if ("Terbaru ke Terlama".equals(selected)) {
                    // DESCENDING: newest first
                    rowSorter.setSortKeys(Collections.singletonList(
                            new RowSorter.SortKey(3, SortOrder.DESCENDING)));
                } else {
                    // ASCENDING: oldest first
                    rowSorter.setSortKeys(Collections.singletonList(
                            new RowSorter.SortKey(3, SortOrder.ASCENDING)));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);
    }

    public void refreshData() {
        loadDashboardData();
    }

    /**
     * Combines the search text filter and the "Jenis Surat" dropdown filter
     * using RowFilter.andFilter. Called by both the search DocumentListener
     * and the combo box ActionListener.
     */
    private void applyFilters() {
        java.util.List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();

        // 1. Text search filter (searches across all columns)
        String text = searchField.getText().trim();
        if (!text.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
        }

        // 2. Jenis Surat filter (column index 2)
        String jenis = (String) filterJenisCombo.getSelectedItem();
        if (jenis != null && !"Semua Jenis".equals(jenis)) {
            filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(jenis) + "$", 2));
        }

        // 3. Apply combined filter
        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else if (filters.size() == 1) {
            rowSorter.setRowFilter(filters.get(0));
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    private void loadDashboardData() {
        SuratMasukDAO suratMasukDAO = new SuratMasukDAO();
        SuratKeluarDAO suratKeluarDAO = new SuratKeluarDAO();

        int totalMasuk = suratMasukDAO.count();
        int totalKeluar = suratKeluarDAO.count();
        int totalSurat = totalMasuk + totalKeluar;
        int suratHariIni = suratMasukDAO.countToday()
                + suratKeluarDAO.countToday();

        totalMasukLabel.setText(String.valueOf(totalMasuk));
        totalKeluarLabel.setText(String.valueOf(totalKeluar));
        totalSuratLabel.setText(String.valueOf(totalSurat));
        suratHariIniLabel.setText(String.valueOf(suratHariIni));

        // semuaSurat: [tanggal, nomorSurat, jenisSurat, perihal, id, pengirim/penerima]
        List<Object[]> semuaSurat = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        List<SuratMasuk> listMasuk = suratMasukDAO.getAll();
        for (SuratMasuk surat : listMasuk) {
            semuaSurat.add(new Object[] {
                    surat.getTanggal(),
                    surat.getNomorSurat(),
                    "Surat Masuk",
                    surat.getPerihal(),
                    surat.getId(),
                    surat.getPengirim()
            });
        }

        List<SuratKeluar> listKeluar = suratKeluarDAO.getAll();
        for (SuratKeluar surat : listKeluar) {
            semuaSurat.add(new Object[] {
                    surat.getTanggal(),
                    surat.getNomorSurat(),
                    "Surat Keluar",
                    surat.getPerihal(),
                    surat.getId(),
                    surat.getPenerima()
            });
        }

        Collections.sort(
                semuaSurat,
                new Comparator<Object[]>() {
                    @Override
                    public int compare(Object[] a, Object[] b) {
                        return ((java.util.Date) b[0])
                                .compareTo((java.util.Date) a[0]);
                    }
                });

        model.setRowCount(0);
        // Store extra data per row for PDF export
        dashboardExtraData.clear();
        int no = 1;
        for (Object[] row : semuaSurat) {
            if (no > 10) {
                break;
            }
            model.addRow(new Object[] {
                    no,
                    row[1],
                    row[2],
                    sdf.format((java.util.Date) row[0]),
                    row[3],
                    "Download PDF"
            });
            // Store: [id, jenisSurat, pengirim/penerima, nomorSurat, tanggalFormatted, perihal]
            dashboardExtraData.add(new Object[] {
                    row[4],       // id
                    row[2],       // jenis surat
                    row[5],       // pengirim/penerima
                    row[1],       // nomor surat
                    sdf.format((java.util.Date) row[0]),  // tanggal
                    row[3]        // perihal
            });
            no++;
        }

        // Set button renderer/editor for Aksi column
        table.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
        table.getColumn("Aksi").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private JPanel createCard(String title, JLabel countLabel, Color bgColor, Color textColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);

        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        countLabel.setForeground(textColor);
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        return card;
    }

    // Extra data per row for PDF generation (parallel to table model rows)
    private final List<Object[]> dashboardExtraData = new ArrayList<>();

    // ==================== Button Renderer ====================
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // ==================== Button Editor ====================
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(JTable tbl, Object value, boolean isSelected,
                int row, int column) {
            this.label = value == null ? "" : value.toString();
            button.setText(label);
            this.clicked = true;
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                try {
                    if ("Download PDF".equals(label) && row < dashboardExtraData.size()) {
                        Object[] extra = dashboardExtraData.get(row);
                        String id = String.valueOf(extra[0]);
                        String jenisSurat = String.valueOf(extra[1]);
                        String pihak = String.valueOf(extra[2]);
                        String nomor = String.valueOf(extra[3]);
                        String tanggalStr = String.valueOf(extra[4]);
                        String perihal = String.valueOf(extra[5]);

                        boolean isMasuk = "Surat Masuk".equals(jenisSurat);
                        String pihakLabel = isMasuk ? "Pengirim" : "Penerima";

                        String[][] fields = {
                                { "ID", id },
                                { "Nomor Surat", nomor },
                                { "Tanggal", tanggalStr },
                                { pihakLabel, pihak },
                                { "Perihal", perihal }
                        };

                        String safePihak = pihak.replaceAll("[^a-zA-Z0-9]", "_");
                        String defaultName = jenisSurat.replace(" ", "_") + "_" + id + "_" + safePihak + ".pdf";

                        JFileChooser chooser = new JFileChooser();
                        chooser.setDialogTitle("Simpan Detail PDF");
                        chooser.setSelectedFile(new java.io.File(defaultName));
                        chooser.setFileFilter(
                                new javax.swing.filechooser.FileNameExtensionFilter(
                                        "PDF Files (*.pdf)", "pdf"));

                        int result = chooser.showSaveDialog(HomePanel.this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            String filePath = chooser.getSelectedFile().getAbsolutePath();
                            if (!filePath.toLowerCase().endsWith(".pdf")) {
                                filePath += ".pdf";
                            }
                            boolean ok = service.PdfExporter.exportDetailToPdf(
                                    "Detail " + jenisSurat, fields, filePath);
                            if (ok) {
                                JOptionPane.showMessageDialog(HomePanel.this,
                                        "PDF berhasil disimpan:\n" + filePath);
                            } else {
                                JOptionPane.showMessageDialog(HomePanel.this,
                                        "Gagal membuat PDF.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(HomePanel.this,
                            "Gagal memproses aksi: " + ex.getMessage());
                }
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
