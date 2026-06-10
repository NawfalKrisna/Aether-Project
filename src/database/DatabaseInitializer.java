package database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {

        try (
            Connection conn =
                DatabaseConnection.getConnection();

            Statement stmt =
                conn.createStatement()
        ) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS surat_masuk (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nomor_surat TEXT," +
                "tanggal TEXT," +
                "pengirim TEXT," +
                "perihal TEXT," +
                "file_path TEXT" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS surat_keluar (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nomor_surat TEXT," +
                "tanggal TEXT," +
                "tujuan TEXT," +
                "perihal TEXT," +
                "file_path TEXT" +
                ")"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}