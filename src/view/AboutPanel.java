package view;

import javax.swing.*;
import java.awt.*;

/**
 * Panel to display information about the application.
 * Extends JPanel.
 */
public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("About Aether Project", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JTextArea aboutText = new JTextArea("Aether Project - Aplikasi Berkas Surat\n\nVersi: 1.0\nDeskripsi: Aplikasi untuk manajemen surat masuk dan surat keluar.");
        aboutText.setEditable(false);
        aboutText.setMargin(new Insets(20, 20, 20, 20));
        add(aboutText, BorderLayout.CENTER);
    }
}
