package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.util.List;
import dao.SuratKeluarDAO;
import model.SuratKeluar;
import utils.FileUploadUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TambahSuratKeluarDialog extends JDialog {

    private JTextField txtNomor;
    private JTextField txtTanggal;
    private JTextField txtTujuan;
    private JTextField txtPerihal;
    private JLabel lblSelectedFile;
    private String selectedFilePath;
    private String existingFilePath;
    private String editingId = "";

    public TambahSuratKeluarDialog(Frame parent) {
        this(parent, null);
    }

    public TambahSuratKeluarDialog(Frame parent, model.SuratKeluar surat) {
        super(parent, surat == null ? "Tambah Surat Keluar" : "Edit Surat Keluar", true);
        setSize(750, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);
        initUI();

        if (surat != null) {
            editingId = surat.getId();
            txtNomor.setText(surat.getNomorSurat());
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            txtTanggal.setText(sdf.format(surat.getTanggal()));
            txtTujuan.setText(surat.getPenerima());
            txtPerihal.setText(surat.getPerihal());
            existingFilePath = surat.getFilePath();
            if (existingFilePath != null && !existingFilePath.isEmpty()) {
                lblSelectedFile.setText("File saat ini: " + new File(existingFilePath).getName());
            }
            // lock nomor on edit
            txtNomor.setEditable(false);
        } else {
            // new entry: auto-generate nomor
            try {
                SuratKeluarDAO dao = new SuratKeluarDAO();
                int next = dao.getNextNomorIndex();
                txtNomor.setText("SK-" + next);
            } catch (Exception ex) {
                txtNomor.setText("SK-" + System.currentTimeMillis());
            }
            txtNomor.setEditable(false);
        }
    }

    private void initUI() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Tambah Surat Keluar");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Lengkapi informasi surat keluar yang akan diarsipkan");
        subtitle.setForeground(Color.GRAY);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitle);

        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nomor Surat"), gbc);

        gbc.gridx = 1;
        txtNomor = new JTextField();
        formPanel.add(txtNomor, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Tanggal"), gbc);

        gbc.gridx = 1;
        txtTanggal = new JTextField();
        txtTanggal.setToolTipText("Format: dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtTanggal.setText(sdf.format(new Date()));
        txtTanggal.setEditable(false);
        txtTanggal.setFocusable(false);
        formPanel.add(txtTanggal, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Tujuan"), gbc);

        gbc.gridx = 1;
        txtTujuan = new JTextField();
        formPanel.add(txtTujuan, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Perihal"), gbc);

        gbc.gridx = 1;
        txtPerihal = new JTextField();
        formPanel.add(txtPerihal, gbc);

        JPanel uploadPanel = new JPanel(new BorderLayout());
        uploadPanel.setBorder(BorderFactory.createDashedBorder(new Color(180, 180, 180)));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadPanel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(
                        DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {

                if (!canImport(support))
                    return false;

                try {

                    Transferable t = support.getTransferable();

                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) t.getTransferData(
                            DataFlavor.javaFileListFlavor);

                    if (!files.isEmpty()) {

                        File file = files.get(0);

                        selectedFilePath = file.getAbsolutePath();

                        lblSelectedFile.setText(
                                "✅ File dipilih: "
                                        + file.getName());

                        lblSelectedFile.setForeground(
                                new Color(22, 163, 74));

                        uploadPanel.setBorder(
                                BorderFactory.createLineBorder(
                                        new Color(22, 163, 74), 2));

                        return true;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return false;
            }
        });

        JLabel uploadIcon = new JLabel("\uD83D\uDCC2");
        uploadIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        uploadIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel uploadLabel = new JLabel(
                "<html><center><b>Klik untuk memilih file</b><br>"
                        + "<span style='color:#888'>atau drag &amp; drop file ke sini</span></center></html>",
                SwingConstants.CENTER);
        uploadLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel uploadCenter = new JPanel();
        uploadCenter.setLayout(new BoxLayout(uploadCenter, BoxLayout.Y_AXIS));
        uploadCenter.setOpaque(false);
        uploadCenter.add(Box.createVerticalGlue());
        uploadIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadCenter.add(uploadIcon);
        uploadCenter.add(Box.createVerticalStrut(8));
        uploadCenter.add(uploadLabel);
        uploadCenter.add(Box.createVerticalGlue());
        uploadPanel.add(uploadCenter, BorderLayout.CENTER);

        lblSelectedFile = new JLabel("Belum ada file dipilih");
        lblSelectedFile.setHorizontalAlignment(SwingConstants.CENTER);
        lblSelectedFile.setForeground(Color.GRAY);
        lblSelectedFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        uploadPanel.add(lblSelectedFile, BorderLayout.SOUTH);

        // --- Shared click handler ---
        MouseAdapter clickHandler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(TambahSuratKeluarDialog.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFilePath = chooser.getSelectedFile().getAbsolutePath();
                    lblSelectedFile.setText("\u2705 File dipilih: " + chooser.getSelectedFile().getName());
                    lblSelectedFile.setForeground(new Color(22, 163, 74));
                    uploadPanel.setBorder(BorderFactory.createLineBorder(new Color(22, 163, 74), 2));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                uploadPanel.setBackground(new Color(239, 246, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                uploadPanel.setBackground(Color.WHITE);
            }
        };

        uploadPanel.addMouseListener(clickHandler);
        uploadCenter.addMouseListener(clickHandler);
        uploadIcon.addMouseListener(clickHandler);
        uploadLabel.addMouseListener(clickHandler);
        lblSelectedFile.addMouseListener(clickHandler);

        // --- Shared DropTarget handler ---
        DropTargetAdapter dropHandler = new DropTargetAdapter() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    uploadPanel.setBorder(BorderFactory.createLineBorder(new Color(37, 99, 235), 2));
                    uploadPanel.setBackground(new Color(239, 246, 255));
                    uploadLabel.setText(
                            "<html><center><b style='color:#2563eb'>Lepaskan file di sini!</b></center></html>");
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                uploadPanel.setBorder(BorderFactory.createDashedBorder(new Color(180, 180, 180)));
                uploadPanel.setBackground(Color.WHITE);
                uploadLabel.setText("<html><center><b>Klik untuk memilih file</b><br>"
                        + "<span style='color:#888'>atau drag &amp; drop file ke sini</span></center></html>");
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();
                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        File droppedFile = files.get(0);
                        selectedFilePath = droppedFile.getAbsolutePath();
                        lblSelectedFile.setText("\u2705 File dipilih: " + droppedFile.getName());
                        lblSelectedFile.setForeground(new Color(22, 163, 74));
                        uploadPanel.setBorder(BorderFactory.createLineBorder(new Color(22, 163, 74), 2));
                        uploadPanel.setBackground(Color.WHITE);
                        uploadLabel.setText("<html><center><b>Klik untuk memilih file</b><br>"
                                + "<span style='color:#888'>atau drag &amp; drop file ke sini</span></center></html>");
                    }
                    dtde.dropComplete(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dtde.dropComplete(false);
                }
            }
        };

        // Pasang DropTarget ke uploadPanel dan semua child-nya
        // (Java DnD tidak propagate ke parent otomatis)
        new DropTarget(uploadPanel, DnDConstants.ACTION_COPY, dropHandler, true);
        new DropTarget(uploadCenter, DnDConstants.ACTION_COPY, dropHandler, true);
        new DropTarget(uploadIcon, DnDConstants.ACTION_COPY, dropHandler, true);
        new DropTarget(uploadLabel, DnDConstants.ACTION_COPY, dropHandler, true);
        new DropTarget(lblSelectedFile, DnDConstants.ACTION_COPY, dropHandler, true);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(uploadPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancel = new JButton("Batal");
        JButton btnSave = new JButton("Simpan");
        btnSave.setBackground(new Color(37, 99, 235));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);

        footer.add(btnCancel);
        footer.add(btnSave);
        add(footer, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> {
            if (txtNomor.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nomor surat wajib diisi!");
                return;
            }
            if (txtTujuan.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tujuan wajib diisi!");
                return;
            }
            if (txtPerihal.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Perihal wajib diisi!");
                return;
            }
            if (editingId.isEmpty() && selectedFilePath == null) {
                JOptionPane.showMessageDialog(this, "Silakan pilih file terlebih dahulu!");
                return;
            }

            try {
                Date tanggal = new SimpleDateFormat("dd/MM/yyyy").parse(txtTanggal.getText());

                String uploadedFilePath = existingFilePath;
                if (selectedFilePath != null) {
                    try {
                        uploadedFilePath = FileUploadUtil.saveFile(selectedFilePath);
                    } catch (Exception fileEx) {
                        JOptionPane.showMessageDialog(this, "Error mengupload file:\n" + fileEx.getMessage());
                        fileEx.printStackTrace();
                        return;
                    }
                }

                SuratKeluar suratObj = new SuratKeluar(
                        editingId == null ? "" : editingId,
                        txtNomor.getText(),
                        tanggal,
                        txtPerihal.getText(),
                        txtTujuan.getText(),
                        uploadedFilePath);

                SuratKeluarDAO dao = new SuratKeluarDAO();
                boolean berhasil;
                if (editingId != null && !editingId.isEmpty()) {
                    berhasil = dao.update(suratObj);
                } else {
                    berhasil = dao.insert(suratObj);
                }

                if (berhasil) {
                    JOptionPane.showMessageDialog(this, "Surat berhasil disimpan!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan surat!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan:\n" + ex.getMessage());
            }
        });
    }
}
