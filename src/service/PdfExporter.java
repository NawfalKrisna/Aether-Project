package service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * PDF Export service using iText 5 (com.itextpdf:itextpdf:5.5.13.x).
 * Generates a formatted PDF document with a title, metadata, and a data table.
 */
public class PdfExporter {

    // ── Colour palette ──────────────────────────────────────────────────────
    private static final BaseColor HEADER_BG = new BaseColor(37, 99, 235); // #2563EB blue
    private static final BaseColor HEADER_FG = BaseColor.WHITE;
    private static final BaseColor ROW_ODD = new BaseColor(248, 250, 252); // #F8FAFC
    private static final BaseColor ROW_EVEN = BaseColor.WHITE;
    private static final BaseColor BORDER_CLR = new BaseColor(226, 232, 240); // #E2E8F0

    // ── Fonts ────────────────────────────────────────────────────────────────
    private static final Font TITLE_FONT = new Font(
            Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(15, 23, 42));

    private static final Font SUBTITLE_FONT = new Font(
            Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(100, 116, 139));

    private static final Font HEADER_FONT = new Font(
            Font.FontFamily.HELVETICA, 11, Font.BOLD, HEADER_FG);

    private static final Font CELL_FONT = new Font(
            Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(15, 23, 42));

    private static final Font FOOTER_FONT = new Font(
            Font.FontFamily.HELVETICA, 8, Font.ITALIC, new BaseColor(148, 163, 184));

    /**
     * Generates a PDF file from the supplied column headers and row data.
     *
     * @param docTitle Title shown at the top of the PDF (e.g. "Surat Masuk")
     * @param columns  Column header names
     * @param data     Row data – each Object[] is one row, values will be converted
     *                 via toString()
     * @param filePath Destination path including ".pdf" extension
     * @return true if the file was written successfully, false otherwise
     */
    public static boolean exportTableToPdf(
            String docTitle,
            String[] columns,
            List<Object[]> data,
            String filePath) {

        // Use A4 Landscape for wider tables
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // ── Document Title ──────────────────────────────────────────────
            Paragraph title = new Paragraph("Laporan " + docTitle, TITLE_FONT);
            title.setAlignment(Element.ALIGN_LEFT);
            title.setSpacingAfter(4f);
            document.add(title);

            // ── Subtitle / metadata line ────────────────────────────────────
            String generatedAt = new SimpleDateFormat("dd MMMM yyyy, HH:mm").format(new Date());
            Paragraph subtitle = new Paragraph(
                    "Diekspor pada: " + generatedAt + "  |  Total baris: " + data.size(),
                    SUBTITLE_FONT);
            subtitle.setSpacingAfter(18f);
            document.add(subtitle);

            // ── Table ───────────────────────────────────────────────────────
            PdfPTable table = new PdfPTable(columns.length);
            table.setWidthPercentage(100f);
            table.setSpacingBefore(4f);

            // Distribute columns evenly (override for specific layouts if needed)
            float[] widths = new float[columns.length];
            for (int i = 0; i < widths.length; i++)
                widths[i] = 1f;
            table.setWidths(widths);

            // ── Header row ──────────────────────────────────────────────────
            for (String col : columns) {
                PdfPCell cell = new PdfPCell(new Phrase(col, HEADER_FONT));
                cell.setBackgroundColor(HEADER_BG);
                cell.setBorderColor(BORDER_CLR);
                cell.setBorderWidth(0.5f);
                cell.setPadding(8f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }

            // ── Data rows ───────────────────────────────────────────────────
            int rowIndex = 0;
            for (Object[] row : data) {
                BaseColor bg = (rowIndex % 2 == 0) ? ROW_EVEN : ROW_ODD;
                for (int c = 0; c < columns.length; c++) {
                    String value = (c < row.length && row[c] != null) ? row[c].toString() : "";
                    PdfPCell cell = new PdfPCell(new Phrase(value, CELL_FONT));
                    cell.setBackgroundColor(bg);
                    cell.setBorderColor(BORDER_CLR);
                    cell.setBorderWidth(0.4f);
                    cell.setPadding(6f);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    // Centre-align the first column (usually ID / No)
                    cell.setHorizontalAlignment(c == 0 ? Element.ALIGN_CENTER : Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
                rowIndex++;
            }

            document.add(table);

            // ── Footer note ─────────────────────────────────────────────────
            Paragraph footer = new Paragraph(
                    "Dokumen ini dibuat secara otomatis oleh Aether Project – Aplikasi Berkas Surat.",
                    FOOTER_FONT);
            footer.setSpacingBefore(14f);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return true;

        } catch (DocumentException | java.io.FileNotFoundException ex) {
            System.err.println("[PdfExporter] Error: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    /**
     * Generates a single-letter detail PDF (form-style vertical layout).
     * Used when the user clicks "Download PDF" on an individual row.
     *
     * @param docTitle e.g. "Detail Surat Masuk"
     * @param fields   Ordered label–value pairs: {{"Nomor Surat","SM-001"},
     *                 {"Tanggal","12/06/2026"}, ...}
     * @param filePath Destination .pdf path
     * @return true on success
     */
    public static boolean exportDetailToPdf(
            String docTitle,
            String[][] fields,
            String filePath) {

        // A4 Portrait – natural for a single document/form
        Document document = new Document(PageSize.A4, 54, 54, 54, 54);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // ── Title ───────────────────────────────────────────────────────
            Paragraph title = new Paragraph(docTitle, TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(4f);
            document.add(title);

            // ── Horizontal rule (thin line) ─────────────────────────────────
            com.itextpdf.text.pdf.draw.LineSeparator line = new com.itextpdf.text.pdf.draw.LineSeparator();
            line.setLineColor(BORDER_CLR);
            line.setLineWidth(1f);
            document.add(new com.itextpdf.text.Chunk(line));
            document.add(new Paragraph(" ")); // spacer

            // ── Generated-at subtitle ───────────────────────────────────────
            String generatedAt = new SimpleDateFormat("dd MMMM yyyy, HH:mm").format(new Date());
            Paragraph subtitle = new Paragraph("Dicetak pada: " + generatedAt, SUBTITLE_FONT);
            subtitle.setSpacingAfter(16f);
            document.add(subtitle);

            // ── Form table (2 columns: Label | Value) ───────────────────────
            PdfPTable formTable = new PdfPTable(2);
            formTable.setWidthPercentage(100f);
            formTable.setWidths(new float[] { 2f, 4f }); // label narrower than value

            Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,
                    new BaseColor(15, 23, 42));
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL,
                    new BaseColor(51, 65, 85));

            for (String[] field : fields) {
                String label = field[0];
                String value = (field.length > 1 && field[1] != null) ? field[1] : "-";

                // Label cell
                PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
                labelCell.setBorder(0);
                labelCell.setPadding(8f);
                labelCell.setBackgroundColor(new BaseColor(241, 245, 249)); // #F1F5F9
                labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                formTable.addCell(labelCell);

                // Value cell
                PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
                valueCell.setBorder(0);
                valueCell.setPadding(8f);
                valueCell.setBackgroundColor(BaseColor.WHITE);
                valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                formTable.addCell(valueCell);
            }

            document.add(formTable);

            // ── Footer ──────────────────────────────────────────────────────
            Paragraph footer = new Paragraph(
                    "Dokumen ini dibuat secara otomatis oleh Aether Project – Aplikasi Berkas Surat.",
                    FOOTER_FONT);
            footer.setSpacingBefore(24f);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return true;

        } catch (DocumentException | java.io.FileNotFoundException ex) {
            System.err.println("[PdfExporter] exportDetailToPdf error: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    /**
     * Legacy overload – kept for backward compatibility with code that passes
     * a List&lt;Surat&gt; (not used by the new Export PDF flow).
     */
    public static boolean exportToPdf(List<model.Surat> data, String filePath) {
        System.out.println("exportToPdf(List<Surat>) called – not implemented in this version.");
        return false;
    }
}
