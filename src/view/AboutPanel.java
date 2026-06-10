package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        initUI();
    }

    private void initUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header
        JLabel title = new JLabel("AETHER PROJECT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Digital Mail Management System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(25));

        // Card About
        mainPanel.add(createCard(
                "📌 About Application",
                "Aether Project adalah aplikasi manajemen surat yang membantu proses administrasi surat masuk dan surat keluar secara digital."
        ));
         mainPanel.add(createCard(
                "📌 Tujuan Application",
                " membantu instansi atau organisasi dalam mengelola dokumen surat secara digital sehingga proses pencatatan dan pencarian data  menjadi lebih efisien, cepat dan mudah."
        ));

        // Card Fitur
        mainPanel.add(createCard(
                "✨ Fitur Utama",
                "✓ Manajemen Surat Masuk\n" +
                "✓ Manajemen Surat Keluar\n" +
                "✓ Arsip Semua Surat\n" +
                "✓ Filter dan Pencarian Data\n" +
                "✓ Sorting Berdasarkan Tanggal\n" +
                "✓ Export PDF"
        ));

        // Card Sistem
        mainPanel.add(createCard(
                "⚙️ Informasi Sistem",
                "Versi      : 1.0\n" +
                "Framework  : Java Swing\n" +
                "Database   : SQLite"
        ));

        // Card Tim
        mainPanel.add(createCard(
                "👨‍💻 Tim Pengembang",
                "Wisnu Septa Hariyanto Putra\n" +
                "Nawfal Krisna Aghazali\n" +
                "Ridhoi Wahyu Saputra\n" +
                "Hana Joma Naumi"
        ));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    // METHOD INI YANG TADI BELUM ADA
    private JPanel createCard(String title, String content) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTextArea txtContent = new JTextArea(content);
        txtContent.setEditable(false);
        txtContent.setBackground(Color.WHITE);
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(txtContent, BorderLayout.CENTER);

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        return card;
    }
}