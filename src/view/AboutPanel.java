package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * AboutPanel digunakan untuk menampilkan informasi
 * mengenai aplikasi Aether Project.
 */
public class AboutPanel extends JPanel {

        /**
         * Constructor AboutPanel
         * Mengatur layout utama dan memanggil method initUI()
         */
        public AboutPanel() {
                setLayout(new BorderLayout());
                setBackground(new Color(245, 247, 250));
                initUI();
        }

        /**
         * Method untuk membangun seluruh tampilan panel About
         */
        private void initUI() {

                // Panel utama sebagai wadah seluruh komponen
                JPanel mainPanel = new JPanel();

                // Menggunakan BoxLayout agar card tersusun vertikal
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

                // Warna background panel
                mainPanel.setBackground(new Color(245, 247, 250));

                // Memberikan jarak dari tepi panel
                mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

                // =========================
                // HEADER APLIKASI
                // =========================

                // Judul utama aplikasi
                JLabel title = new JLabel("AETHER PROJECT");
                title.setFont(new Font("Segoe UI", Font.BOLD, 32));
                title.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Subjudul aplikasi
                JLabel subtitle = new JLabel("Digital Mail Management System");
                subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                subtitle.setForeground(Color.GRAY);
                subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Menambahkan komponen ke panel utama
                mainPanel.add(title);
                mainPanel.add(Box.createVerticalStrut(5)); // Jarak vertikal
                mainPanel.add(subtitle);
                mainPanel.add(Box.createVerticalStrut(25));

                // =========================
                // CARD ABOUT APPLICATION
                // =========================
                mainPanel.add(createCard(
                                "📌 About Application",
                                "Aether Project adalah aplikasi manajemen surat yang membantu proses administrasi surat masuk dan surat keluar secara digital."));

                // =========================
                // CARD TUJUAN APLIKASI
                // =========================
                mainPanel.add(createCard(
                                "📌 Tujuan Application",
                                "Membantu instansi atau organisasi dalam mengelola dokumen surat secara digital sehingga proses pencatatan dan pencarian data menjadi lebih efisien, cepat dan mudah."));

                // =========================
                // CARD FITUR UTAMA
                // =========================
                mainPanel.add(createCard(
                                "✨ Fitur Utama",
                                "✓ Manajemen Surat Masuk\n" +
                                                "✓ Manajemen Surat Keluar\n" +
                                                "✓ Arsip Semua Surat\n" +
                                                "✓ Filter dan Pencarian Data\n" +
                                                "✓ Sorting Berdasarkan Tanggal\n" +
                                                "✓ Export PDF"));

                // =========================
                // CARD INFORMASI SISTEM
                // =========================
                mainPanel.add(createCard(
                                "⚙️ Informasi Sistem",
                                "Versi      : 1.0\n" +
                                                "Framework  : Java Swing\n" +
                                                "Database   : SQLite"));

                // =========================
                // CARD TECH STACK
                // =========================
                mainPanel.add(createCard(
                                "🛠️ Tech Stack",
                                "Java 8\n" +
                                                "Swing\n" +
                                                "FlatLaf (UI)\n" +
                                                "SQLite (JDBC)\n" +
                                                "iText PDF"));

                // =========================
                // CARD TIM PENGEMBANG
                // =========================
                mainPanel.add(createCard(
                                "Tim Pengembang",
                                "Backend Developer\n" +
                                                "✓ Wisnu Septa Harianto Putra\n\n" +

                                                "Full Stack Developer\n" +
                                                "✓ Hana Joma Naomi\n\n" +

                                                "Frontend Developer\n" +
                                                "✓ Nawfal Krisna Aghafazli\n\n" +

                                                "UI / UX Designer\n" +
                                                "✓ Ridhoi Wahyu Saputra"));
                // ScrollPane digunakan agar isi panel dapat digulir
                // jika ukuran jendela lebih kecil dari isi panel
                JScrollPane scrollPane = new JScrollPane(mainPanel);

                // Menghilangkan border bawaan ScrollPane
                scrollPane.setBorder(null);

                // Menambahkan ScrollPane ke panel utama
                add(scrollPane, BorderLayout.CENTER);
        }

        /**
         * Method untuk membuat card informasi
         *
         * @param title   Judul card
         * @param content Isi card
         * @return JPanel card yang sudah jadi
         */
        private JPanel createCard(String title, String content) {

                // Membuat panel card dengan BorderLayout
                JPanel card = new JPanel(new BorderLayout());

                // Warna latar card
                card.setBackground(Color.WHITE);

                // Border luar dan padding dalam card
                card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                                new EmptyBorder(15, 15, 15, 15)));

                // Label judul card
                JLabel lblTitle = new JLabel(title);
                lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

                // Area teks isi card
                JTextArea txtContent = new JTextArea(content);
                txtContent.setEditable(false);
                txtContent.setBackground(Color.WHITE);
                txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                if (title.equals("Tim Pengembang")) {
                        card.setBackground(new Color(235, 245, 255));
                        txtContent.setBackground(new Color(235, 245, 255));
                        lblTitle.setForeground(new Color(0, 102, 204));
                        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                }

                // Membuat teks otomatis turun baris
                txtContent.setLineWrap(true);
                txtContent.setWrapStyleWord(true);

                // Menambahkan komponen ke card
                card.add(lblTitle, BorderLayout.NORTH);
                card.add(txtContent, BorderLayout.CENTER);

                // Mengatur ukuran maksimum card
                if (title.equals("Tim Pengembang")) {
                        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
                } else {
                        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
                }

                return card;
        }
}