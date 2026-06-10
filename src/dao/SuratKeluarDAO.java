package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.SuratKeluar;
import java.util.ArrayList;
import java.util.List;

public class SuratKeluarDAO {

    public int count() {

        String sql =
                "SELECT COUNT(*) total FROM surat_keluar";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int countToday() {

        String sql =
                "SELECT COUNT(*) total FROM surat_keluar WHERE tanggal = DATE('now') ";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<SuratKeluar> getAll() {

        List<SuratKeluar> list =
                new ArrayList<>();

        String sql =
                "SELECT * FROM surat_keluar ORDER BY tanggal DESC";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)
        ) {

            while (rs.next()) {
                try {
                    String id = rs.getString("id");
                    String nomorSurat = rs.getString("nomor_surat");
                    String tanggalStr = rs.getString("tanggal");
                    String perihal = rs.getString("perihal");
                    String tujuan = rs.getString("tujuan");
                    String filePath = rs.getString("file_path");

                    java.util.Date tanggal = null;
                    
                    // Try multiple date formats for backward compatibility
                    try {
                        tanggal = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(tanggalStr);
                    } catch (Exception e1) {
                        try {
                            // Handle Java Date toString format: "Wed Jun 10 00:00:00 WIB 2026"
                            tanggal = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", java.util.Locale.ENGLISH).parse(tanggalStr);
                        } catch (Exception e2) {
                            System.err.println("[SuratKeluarDAO] Could not parse date: " + tanggalStr);
                            tanggal = new java.util.Date();
                        }
                    }

                    SuratKeluar surat = new SuratKeluar(
                            id,
                            nomorSurat,
                            tanggal,
                            perihal,
                            tujuan,
                            filePath
                    );

                    list.add(surat);
                } catch (Exception parseEx) {
                    System.err.println("[SuratKeluarDAO] Error parsing row: " + parseEx.getMessage());
                    parseEx.printStackTrace();
                }
            }
            

                } catch (Exception e) {
                        e.printStackTrace();
                }

        return list;
    }

    public boolean insert(SuratKeluar surat) {

        String sql =
                "INSERT INTO surat_keluar " +
                        "(nomor_surat, tanggal, tujuan, perihal, file_path) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();
        ) {
            if (count() == 0) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='surat_keluar'");
                } catch (Exception ignore) {
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        surat.getNomorSurat()
                );

                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd");

                ps.setString(
                        2,
                        sdf.format(
                                surat.getTanggal()
                        )
                );

                ps.setString(
                        3,
                        surat.getPenerima()
                );

                ps.setString(
                        4,
                        surat.getPerihal()
                );

                ps.setString(
                        5,
                        surat.getFilePath()
                );

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

        public boolean update(SuratKeluar surat) {

                String sql = "UPDATE surat_keluar SET nomor_surat = ?, tanggal = ?, tujuan = ?, perihal = ?, file_path = ? WHERE id = ?";

                try (
                                Connection conn = DatabaseConnection.getConnection();
                                PreparedStatement ps = conn.prepareStatement(sql)
                ) {

                        ps.setString(1, surat.getNomorSurat());

                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        ps.setString(2, sdf.format(surat.getTanggal()));

                        ps.setString(3, surat.getPenerima());
                        ps.setString(4, surat.getPerihal());
                        ps.setString(5, surat.getFilePath());
                        ps.setString(6, surat.getId());

                        return ps.executeUpdate() > 0;

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return false;
        }

        public boolean delete(String id) {

                String selectSql = "SELECT file_path FROM surat_keluar WHERE id = ?";
                String deleteSql = "DELETE FROM surat_keluar WHERE id = ?";

                try (Connection conn = DatabaseConnection.getConnection()) {

                        // get file path and delete file
                        try (PreparedStatement sel = conn.prepareStatement(selectSql)) {
                                sel.setString(1, id);
                                try (ResultSet rs = sel.executeQuery()) {
                                        if (rs.next()) {
                                                String filePath = rs.getString("file_path");
                                                if (filePath != null && !filePath.isEmpty()) {
                                                        tryDeleteFile(filePath);
                                                }
                                        }
                                }
                        }

                        try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                                ps.setString(1, id);
                                int affected = ps.executeUpdate();

                                if (count() == 0) {
                                        try (Statement stmt = conn.createStatement()) {
                                                stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='surat_keluar'");
                                        } catch (Exception ignore) {
                                        }
                                }

                                return affected > 0;
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return false;
        }

        public int getNextNomorIndex() {
                String sql = "SELECT MAX(CAST(SUBSTR(nomor_surat,4) AS INTEGER)) mx FROM surat_keluar WHERE nomor_surat LIKE 'SK-%'";
                try (
                                Connection conn = DatabaseConnection.getConnection();
                                Statement stmt = conn.createStatement();
                                ResultSet rs = stmt.executeQuery(sql)
                ) {
                        if (rs.next()) {
                                int mx = rs.getInt("mx");
                                return mx + 1;
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return 1;
        }

        private void tryDeleteFile(String filePath) {
                if (filePath == null || filePath.trim().isEmpty()) return;
                try {
                        Path p1 = Paths.get(filePath);
                        java.nio.file.Files.deleteIfExists(p1);
                        return;
                } catch (Exception ignored) {
                }

                try {
                        Path p2 = Paths.get(new File(filePath).getAbsolutePath());
                        java.nio.file.Files.deleteIfExists(p2);
                        return;
                } catch (Exception ignored) {
                }

                try {
                        Path p3 = Paths.get(System.getProperty("user.dir"), filePath);
                        java.nio.file.Files.deleteIfExists(p3);
                        return;
                } catch (Exception ignored) {
                }
        }
}