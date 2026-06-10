package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        uploadPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel uploadLabel = new JLabel("Klik untuk memilih file");
        uploadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uploadPanel.add(uploadLabel, BorderLayout.CENTER);

        lblSelectedFile = new JLabel("Belum ada file dipilih");
        lblSelectedFile.setHorizontalAlignment(SwingConstants.CENTER);
        lblSelectedFile.setForeground(Color.GRAY);
        uploadPanel.add(lblSelectedFile, BorderLayout.SOUTH);

        uploadPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(TambahSuratKeluarDialog.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFilePath = chooser.getSelectedFile().getAbsolutePath();
                    lblSelectedFile.setText("File dipilih : " + chooser.getSelectedFile().getName());
                }
            }
        });

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
                        uploadedFilePath
                );

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
