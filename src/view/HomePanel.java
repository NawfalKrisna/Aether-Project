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
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;

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

        JPanel searchRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchRight.setOpaque(false);
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
                "Perihal"
        };

        model = new DefaultTableModel(columns, 0);
        rowSorter = new TableRowSorter<>(model);
        JTable table = new JTable(model);
        table.setRowSorter(rowSorter);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));

        // Wire up search
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void doFilter() {
                String text = searchField.getText().trim();
                if (text.isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
                }
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                doFilter();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                doFilter();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                doFilter();
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

        List<Object[]> semuaSurat = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        List<SuratMasuk> listMasuk = suratMasukDAO.getAll();
        for (SuratMasuk surat : listMasuk) {
            semuaSurat.add(new Object[] {
                    surat.getTanggal(),
                    surat.getNomorSurat(),
                    "Surat Masuk",
                    surat.getPerihal()
            });
        }

        List<SuratKeluar> listKeluar = suratKeluarDAO.getAll();
        for (SuratKeluar surat : listKeluar) {
            semuaSurat.add(new Object[] {
                    surat.getTanggal(),
                    surat.getNomorSurat(),
                    "Surat Keluar",
                    surat.getPerihal()
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
        int no = 1;
        for (Object[] row : semuaSurat) {
            if (no > 10) {
                break;
            }
            model.addRow(new Object[] {
                    no++,
                    row[1],
                    row[2],
                    sdf.format((java.util.Date) row[0]),
                    row[3]
            });
        }
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
}
