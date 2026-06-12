package view;

import service.PdfExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for PDF Export preview and download.
 * Displays a read-only preview of the data to be exported,
 * and provides a "Download PDF" button that generates the file
 * via iText at a user-chosen location.
 */
public class ExportPdfPanel extends JPanel {

    private JTable previewTable;
    private DefaultTableModel previewModel;
    private JLabel statusLabel;
    private JLabel countLabel;

    // Holds the current export payload
    private String[] currentColumns;
    private List<Object[]> currentData;
    private String currentTitle;

    public ExportPdfPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        initUI();
    }

    private void initUI() {
        // ── Header ──────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Export Data ke PDF");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnBack = new JButton("\u2190 Kembali");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof DashboardFrame) {
                // Navigate back to Dashboard
                ((DashboardFrame) frame).showDashboard();
            }
        });

        headerPanel.add(btnBack, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // ── Table Container ─────────────────────────────────────────────────
        JPanel tableContainer = new JPanel(new BorderLayout(0, 12));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Sub-header row: document title + row count
        JPanel subHeaderPanel = new JPanel(new BorderLayout());
        subHeaderPanel.setOpaque(false);

        JLabel docTitleLabel = new JLabel("Preview Data");
        docTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        countLabel = new JLabel("");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(new Color(100, 116, 139));

        subHeaderPanel.add(docTitleLabel, BorderLayout.WEST);
        subHeaderPanel.add(countLabel, BorderLayout.EAST);

        tableContainer.add(subHeaderPanel, BorderLayout.NORTH);

        // Read-only preview table
        previewModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // fully read-only
            }
        };

        previewTable = new JTable(previewModel);
        previewTable.setRowHeight(36);
        previewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        previewTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        previewTable.setShowVerticalLines(false);
        previewTable.setGridColor(new Color(226, 232, 240));
        previewTable.getTableHeader().setReorderingAllowed(false);
        previewTable.setSelectionBackground(new Color(230, 240, 255));
        previewTable.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(previewTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // ── Bottom action bar ────────────────────────────────────────────────
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setOpaque(false);
        actionBar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(new Color(100, 116, 139));

        JButton btnDownload = new JButton("\uD83D\uDCC4  Download PDF");
        btnDownload.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDownload.setBackground(new Color(22, 163, 74)); // Green
        btnDownload.setForeground(Color.WHITE);
        btnDownload.setFocusPainted(false);
        btnDownload.setBorderPainted(false);
        btnDownload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDownload.addActionListener(e -> handleDownload());

        actionBar.add(statusLabel, BorderLayout.WEST);
        actionBar.add(btnDownload, BorderLayout.EAST);

        tableContainer.add(actionBar, BorderLayout.SOUTH);

        add(tableContainer, BorderLayout.CENTER);
    }

    /**
     * Called by DashboardFrame.showExportPdf() to populate the preview table
     * with data from the calling panel (SuratMasuk / SuratKeluar).
     *
     * @param columns  Column header names (display columns only, e.g. without
     *                 Edit/Delete)
     * @param data     List of row arrays matching the columns
     * @param docTitle Document title, e.g. "Surat Masuk" or "Surat Keluar"
     */
    public void loadData(String[] columns, List<Object[]> data, String docTitle) {
        this.currentColumns = columns;
        this.currentData = data;
        this.currentTitle = docTitle;

        // Rebuild the table model
        previewModel.setRowCount(0);
        previewModel.setColumnCount(0);
        for (String col : columns) {
            previewModel.addColumn(col);
        }
        for (Object[] row : data) {
            previewModel.addRow(row);
        }

        countLabel.setText(data.size() + " baris data");
        statusLabel.setText("Siap di-export: " + docTitle);
    }

    /**
     * Opens a JFileChooser, generates the PDF via PdfExporter, and shows feedback.
     */
    private void handleDownload() {
        if (currentData == null || currentData.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada data untuk di-export.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan PDF Sebagai");
        chooser.setSelectedFile(new File(currentTitle.replace(" ", "_") + "_Export.pdf"));
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return; // user cancelled
        }

        File selectedFile = chooser.getSelectedFile();
        String rawPath = selectedFile.getAbsolutePath();
        final String filePath = rawPath.toLowerCase().endsWith(".pdf")
                ? rawPath
                : rawPath + ".pdf";

        statusLabel.setText("Generating PDF...");

        // Run PDF generation in a background thread to keep UI responsive
        new SwingWorker<Boolean, Void>() {
            private String errorMessage;

            @Override
            protected Boolean doInBackground() {
                try {
                    return PdfExporter.exportTableToPdf(
                            currentTitle,
                            currentColumns,
                            currentData,
                            filePath);
                } catch (Exception ex) {
                    errorMessage = ex.getMessage();
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        statusLabel.setText("PDF berhasil disimpan: " + filePath);
                        statusLabel.setForeground(new Color(22, 163, 74));
                        int open = JOptionPane.showConfirmDialog(
                                ExportPdfPanel.this,
                                "PDF berhasil disimpan!\n\n" + filePath + "\n\nBuka file sekarang?",
                                "Berhasil",
                                JOptionPane.YES_NO_OPTION);
                        if (open == JOptionPane.YES_OPTION) {
                            try {
                                Desktop.getDesktop().open(new File(filePath));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        statusLabel.setText("Gagal membuat PDF.");
                        statusLabel.setForeground(new Color(220, 38, 38));
                        JOptionPane.showMessageDialog(
                                ExportPdfPanel.this,
                                "Gagal membuat PDF:\n" + (errorMessage != null ? errorMessage : "Unknown error"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }
}
