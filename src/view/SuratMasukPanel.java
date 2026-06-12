package view;

import dao.SuratMasukDAO;
import model.SuratMasuk;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SuratMasukPanel extends JPanel {

        private JTable table;
        private DefaultTableModel model;
        private TableRowSorter<DefaultTableModel> rowSorter;
        private JTextField searchField;

        public SuratMasukPanel() {
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

                JLabel title = new JLabel("Surat Masuk");
                title.setFont(new Font("Segoe UI", Font.BOLD, 24));

                JButton btnAdd = new JButton("+ Tambah Surat Masuk");
                btnAdd.addActionListener(e -> {

                        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);

                        TambahSuratMasukDialog dialog = new TambahSuratMasukDialog(frame);

                        dialog.setVisible(true);
                        loadData();
                        if (frame instanceof DashboardFrame) {
                                ((DashboardFrame) frame).refreshHome();
                        }

                });

                btnAdd.setBackground(new Color(37, 99, 235)); // Primary blue
                btnAdd.setForeground(Color.WHITE);
                btnAdd.setFocusPainted(false);
                btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

                headerPanel.add(title, BorderLayout.WEST);
                headerPanel.add(btnAdd, BorderLayout.EAST);

                add(headerPanel, BorderLayout.NORTH);

                // Table Container
                JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
                tableContainer.setBackground(Color.WHITE);
                tableContainer.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

                // Search Bar + Sort Combo
                JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
                searchPanel.setOpaque(false);
                searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

                searchField = new JTextField();
                searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                searchField.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
                searchField.setToolTipText("Cari surat masuk...");
                searchField.setPreferredSize(new Dimension(220, 30));

                JLabel searchLabel = new JLabel("\uD83D\uDD0D  Cari:");
                searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                searchLabel.setForeground(new Color(100, 116, 139));

                JPanel searchRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
                searchRight.setOpaque(false);
                searchRight.add(searchLabel);
                searchRight.add(searchField);

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

                JPanel sortLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                sortLeft.setOpaque(false);
                sortLeft.add(sortLabel);
                sortLeft.add(sortDateCombo);

                searchPanel.add(sortLeft, BorderLayout.WEST);
                searchPanel.add(searchRight, BorderLayout.EAST);
                tableContainer.add(searchPanel, BorderLayout.NORTH);

                // Placeholder for Table
                String[] columnNames = {
                                "ID",
                                "Nomor Surat",
                                "Tanggal",
                                "Pengirim",
                                "Perihal",
                                "File",
                                "Edit",
                                "Delete"
                };

                model = new DefaultTableModel(columnNames, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return column == 6 || column == 7;
                        }
                };

                rowSorter = new TableRowSorter<>(model);

                // Custom comparator: parse "dd/MM/yyyy" strings into real Date objects
                // so sorting is chronological, NOT alphabetical.
                rowSorter.setComparator(2, new Comparator<String>() {
                        private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        @Override
                        public int compare(String s1, String s2) {
                                try {
                                        Date d1 = sdf.parse(s1);
                                        Date d2 = sdf.parse(s2);
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

                table.setRowHeight(40);
                table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
                table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                table.setShowVerticalLines(false);
                table.setGridColor(new Color(226, 232, 240));

                // Wire up search field to filter table
                searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                        private void doFilter() {
                                String text = searchField.getText().trim();
                                if (text.isEmpty()) {
                                        rowSorter.setRowFilter(null);
                                } else {
                                        rowSorter.setRowFilter(RowFilter
                                                        .regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
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

                // Sort combo box listener – apply sort order on the date column (index 2)
                sortDateCombo.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                String selected = (String) sortDateCombo.getSelectedItem();
                                if ("Terbaru ke Terlama".equals(selected)) {
                                        // DESCENDING: newest first
                                        rowSorter.setSortKeys(java.util.Collections.singletonList(
                                                        new RowSorter.SortKey(2, SortOrder.DESCENDING)));
                                } else {
                                        // ASCENDING: oldest first
                                        rowSorter.setSortKeys(java.util.Collections.singletonList(
                                                        new RowSorter.SortKey(2, SortOrder.ASCENDING)));
                                }
                        }
                });

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
                                                        String pengirim = String.valueOf(model.getValueAt(row, 3));
                                                        String perihal = String.valueOf(model.getValueAt(row, 4));
                                                        String filePath = String.valueOf(model.getValueAt(row, 5));

                                                        try {
                                                                Date tanggal = new SimpleDateFormat("dd/MM/yyyy")
                                                                                .parse(tanggalStr);
                                                                model.SuratMasuk surat = new model.SuratMasuk(id, nomor,
                                                                                tanggal, perihal, pengirim, filePath);
                                                                Frame frame = (Frame) SwingUtilities.getWindowAncestor(
                                                                                SuratMasukPanel.this);
                                                                TambahSuratMasukDialog dialog = new TambahSuratMasukDialog(
                                                                                frame, surat);
                                                                dialog.setVisible(true);
                                                                loadData();
                                                                if (frame instanceof DashboardFrame) {
                                                                        ((DashboardFrame) frame).refreshHome();
                                                                }
                                                        } catch (Exception ex) {
                                                                ex.printStackTrace();
                                                                JOptionPane.showMessageDialog(SuratMasukPanel.this,
                                                                                "Gagal memproses data: "
                                                                                                + ex.getMessage());
                                                        }
                                                });

                                                del.addActionListener(ae -> {
                                                        String id = String.valueOf(model.getValueAt(row, 0));
                                                        int conf = JOptionPane.showConfirmDialog(SuratMasukPanel.this,
                                                                        "Hapus surat ini?", "Konfirmasi",
                                                                        JOptionPane.YES_NO_OPTION);
                                                        if (conf == JOptionPane.YES_OPTION) {
                                                                dao.SuratMasukDAO d = new dao.SuratMasukDAO();
                                                                boolean ok = d.delete(id);
                                                                if (ok) {
                                                                        loadData();
                                                                        Frame frame = (Frame) SwingUtilities
                                                                                        .getWindowAncestor(
                                                                                                        SuratMasukPanel.this);
                                                                        if (frame instanceof DashboardFrame) {
                                                                                ((DashboardFrame) frame).refreshHome();
                                                                        }
                                                                } else {
                                                                        JOptionPane.showMessageDialog(
                                                                                        SuratMasukPanel.this,
                                                                                        "Gagal menghapus data");
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
                                                                java.awt.Desktop.getDesktop()
                                                                                .open(new java.io.File(filePath));
                                                        } catch (Exception ex) {
                                                                JOptionPane.showMessageDialog(SuratMasukPanel.this,
                                                                                "Gagal membuka file: "
                                                                                                + ex.getMessage());
                                                        }
                                                }
                                        }
                                }
                        }
                });
        }

        private void loadData() {

                model.setRowCount(0);

                SuratMasukDAO dao = new SuratMasukDAO();

                List<SuratMasuk> list = dao.getAll();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                for (SuratMasuk surat : list) {
                        model.addRow(new Object[] {
                                        surat.getId(),
                                        surat.getNomorSurat(),
                                        sdf.format(surat.getTanggal()),
                                        surat.getPengirim(),
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
                public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                boolean hasFocus, int row, int column) {
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
                public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
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
                                        String id = String.valueOf(model.getValueAt(row, 0));
                                        if ("Edit".equals(label)) {
                                                String nomor = String.valueOf(model.getValueAt(row, 1));
                                                String tanggalStr = String.valueOf(model.getValueAt(row, 2));
                                                String pengirim = String.valueOf(model.getValueAt(row, 3));
                                                String perihal = String.valueOf(model.getValueAt(row, 4));
                                                String filePath = String.valueOf(model.getValueAt(row, 5));

                                                java.util.Date tanggal = new SimpleDateFormat("dd/MM/yyyy")
                                                                .parse(tanggalStr);
                                                model.SuratMasuk surat = new model.SuratMasuk(id, nomor, tanggal,
                                                                perihal, pengirim, filePath);
                                                Frame frame = (Frame) SwingUtilities
                                                                .getWindowAncestor(SuratMasukPanel.this);
                                                TambahSuratMasukDialog dialog = new TambahSuratMasukDialog(frame,
                                                                surat);
                                                dialog.setVisible(true);
                                                loadData();
                                                if (frame instanceof DashboardFrame) {
                                                        ((DashboardFrame) frame).refreshHome();
                                                }
                                        } else if ("Delete".equals(label)) {
                                                int conf = JOptionPane.showConfirmDialog(SuratMasukPanel.this,
                                                                "Hapus surat ini?", "Konfirmasi",
                                                                JOptionPane.YES_NO_OPTION);
                                                if (conf == JOptionPane.YES_OPTION) {
                                                        dao.SuratMasukDAO d = new dao.SuratMasukDAO();
                                                        boolean ok = d.delete(id);
                                                        if (ok) {
                                                                loadData();
                                                                Frame frame = (Frame) SwingUtilities.getWindowAncestor(
                                                                                SuratMasukPanel.this);
                                                                if (frame instanceof DashboardFrame) {
                                                                        ((DashboardFrame) frame).refreshHome();
                                                                }
                                                        } else {
                                                                JOptionPane.showMessageDialog(SuratMasukPanel.this,
                                                                                "Gagal menghapus data");
                                                        }
                                                }
                                        }
                                } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(SuratMasukPanel.this,
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
