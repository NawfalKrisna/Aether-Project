package view;

import service.SuratService;
import model.Surat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class HomePanel extends JPanel {
    private SuratService suratService;

    public HomePanel() {
        this.suratService = new SuratService();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        initUI();
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

        cardsPanel.add(createCard("Surat Masuk", "10", new Color(220, 252, 231), new Color(22, 163, 74)));
        cardsPanel.add(createCard("Surat Keluar", "10", new Color(224, 242, 254), new Color(2, 132, 199)));
        cardsPanel.add(createCard("Total Surat", "20", new Color(243, 232, 255), new Color(147, 51, 234)));
        cardsPanel.add(createCard("Surat Hari Ini", "0", new Color(255, 237, 213), new Color(234, 88, 12)));

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
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel tableTitle = new JLabel("Surat Terbaru");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tableContainer.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"No", "Nomor Surat", "Jenis Surat", "Tanggal", "Perihal"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        List<Surat> allSurat = suratService.getAllSurat();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        int no = 1;
        for (Surat s : allSurat) {
            if (no > 10) break; // show only 10 recent
            model.addRow(new Object[]{no++, s.getNomorSurat(), s.getJenisSurat(), sdf.format(s.getTanggal()), s.getPerihal()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String count, Color bgColor, Color textColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        
        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        countLabel.setForeground(textColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        return card;
    }
}
