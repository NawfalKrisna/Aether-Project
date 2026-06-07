package view;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for PDF Export functionality.
 * Extends JPanel.
 */
public class ExportPdfPanel extends JPanel {

    public ExportPdfPanel() {
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("Export Data ke PDF", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.add(new JLabel("Opsi Export akan diletakkan di sini."));
        centerPanel.add(new JButton("Preview PDF"));
        add(centerPanel, BorderLayout.CENTER);
    }
}
