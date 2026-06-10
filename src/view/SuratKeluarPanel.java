package view;

import dao.SuratKeluarDAO;
import model.SuratKeluar;

import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.awt.Frame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SuratKeluarPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public SuratKeluarPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        initUI();
        loadData();
    }

    private void initUI() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Surat Keluar");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JButton btnAdd = new JButton("+ Tambah Surat Keluar");
        btnAdd.setBackground(new Color(37, 99, 235)); // Primary blue
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnAdd.addActionListener(e -> {
                        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
                        TambahSuratKeluarDialog dialog = new TambahSuratKeluarDialog(frame);
                        dialog.setVisible(true);
                        loadData();
                        if (frame instanceof DashboardFrame) {
                                ((DashboardFrame) frame).refreshHome();
                        }
                });
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnAdd, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // Table Container
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Placeholder for Table
        String[] columnNames = {
                "ID",
                "Nomor Surat",
                "Tanggal",
                "Penerima",
                "Perihal",
                "File",
                "Edit",
                "Delete"
        };

        model = new DefaultTableModel(columnNames, 0);

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        // Context menu for edit/delete and double-click to open file
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem edit = new JMenuItem("Edit");
                        JMenuItem del = new JMenuItem("Delete");

                        edit.addActionListener(ae -> {
                            String id = String.valueOf(model.getValueAt(row, 0));
                            String nomor = String.valueOf(model.getValueAt(row, 1));
                            String tanggalStr = String.valueOf(model.getValueAt(row, 2));
                            String penerima = String.valueOf(model.getValueAt(row, 3));
                            String perihal = String.valueOf(model.getValueAt(row, 4));
                            String filePath = String.valueOf(model.getValueAt(row, 5));

                            try {
                                Date tanggal = new SimpleDateFormat("dd/MM/yyyy").parse(tanggalStr);
                                model.SuratKeluar surat = new model.SuratKeluar(id, nomor, tanggal, perihal, penerima, filePath);
                                Frame frame = (Frame) SwingUtilities.getWindowAncestor(SuratKeluarPanel.this);
                                TambahSuratKeluarDialog dialog = new TambahSuratKeluarDialog(frame, surat);
                                dialog.setVisible(true);
                                loadData();
                                if (frame instanceof DashboardFrame) {
                                    ((DashboardFrame) frame).refreshHome();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(SuratKeluarPanel.this, "Gagal memproses data: " + ex.getMessage());
                            }
                        });

                        del.addActionListener(ae -> {
                            String id = String.valueOf(model.getValueAt(row, 0));
                            int conf = JOptionPane.showConfirmDialog(SuratKeluarPanel.this, "Hapus surat ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                            if (conf == JOptionPane.YES_OPTION) {
                                dao.SuratKeluarDAO d = new dao.SuratKeluarDAO();
                                boolean ok = d.delete(id);
                                if (ok) {
                                    loadData();
                                    Frame frame = (Frame) SwingUtilities.getWindowAncestor(SuratKeluarPanel.this);
                                    if (frame instanceof DashboardFrame) {
                                        ((DashboardFrame) frame).refreshHome();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(SuratKeluarPanel.this, "Gagal menghapus data");
                                }
                            }
                        });

                        menu.add(edit);
                        menu.add(del);
                        menu.show(table, e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    // file column index is 5
                    if (col == 5 && row >= 0) {
                        String filePath = String.valueOf(model.getValueAt(row, 5));
                        if (filePath != null && !filePath.isEmpty()) {
                            try {
                                java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(SuratKeluarPanel.this, "Gagal membuka file: " + ex.getMessage());
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadData() {

    model.setRowCount(0);

    SuratKeluarDAO dao =
            new SuratKeluarDAO();

    List<SuratKeluar> list =
            dao.getAll();

    SimpleDateFormat sdf =
            new SimpleDateFormat("dd/MM/yyyy");

        for (SuratKeluar surat : list) {
                model.addRow(new Object[]{
                                surat.getId(),
                                surat.getNomorSurat(),
                                sdf.format(surat.getTanggal()),
                                surat.getPenerima(),
                                surat.getPerihal(),
                                surat.getFilePath(),
                                "Edit",
                                "Delete"
                });
        }

        // Add button renderers/editors for Edit/Delete
        table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));
}

        // Renderer for buttons
        class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
                public ButtonRenderer() {
                        setOpaque(true);
                }

                @Override
                public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        setText(value == null ? "" : value.toString());
                        return this;
                }
        }

        // Editor for buttons
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
                public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
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
                                        String id = String.valueOf(model.getValueAt(row, 0));
                                        if ("Edit".equals(label)) {
                                                String nomor = String.valueOf(model.getValueAt(row, 1));
                                                String tanggalStr = String.valueOf(model.getValueAt(row, 2));
                                                String penerima = String.valueOf(model.getValueAt(row, 3));
                                                String perihal = String.valueOf(model.getValueAt(row, 4));
                                                String filePath = String.valueOf(model.getValueAt(row, 5));

                                                java.util.Date tanggal = new SimpleDateFormat("dd/MM/yyyy").parse(tanggalStr);
                                                model.SuratKeluar surat = new model.SuratKeluar(id, nomor, tanggal, perihal, penerima, filePath);
                                                Frame frame = (Frame) SwingUtilities.getWindowAncestor(SuratKeluarPanel.this);
                                                TambahSuratKeluarDialog dialog = new TambahSuratKeluarDialog(frame, surat);
                                                dialog.setVisible(true);
                                                loadData();
                                                if (frame instanceof DashboardFrame) {
                                                        ((DashboardFrame) frame).refreshHome();
                                                }
                                        } else if ("Delete".equals(label)) {
                                                int conf = JOptionPane.showConfirmDialog(SuratKeluarPanel.this, "Hapus surat ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                                                if (conf == JOptionPane.YES_OPTION) {
                                                        dao.SuratKeluarDAO d = new dao.SuratKeluarDAO();
                                                        boolean ok = d.delete(id);
                                                        if (ok) {
                                                                loadData();
                                                                Frame frame = (Frame) SwingUtilities.getWindowAncestor(SuratKeluarPanel.this);
                                                                if (frame instanceof DashboardFrame) {
                                                                        ((DashboardFrame) frame).refreshHome();
                                                                }
                                                        } else {
                                                                JOptionPane.showMessageDialog(SuratKeluarPanel.this, "Gagal menghapus data");
                                                        }
                                                }
                                        }
                                } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(SuratKeluarPanel.this, "Gagal memproses aksi: " + ex.getMessage());
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
