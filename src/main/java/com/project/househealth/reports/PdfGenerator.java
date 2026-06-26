package com.project.househealth.reports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.project.househealth.entity.HealthLog;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGenerator {

    // ── Brand palette (mirrors HouseHealth CSS vars) ──────────────────────────

    /** --hh-green: #3db562 */
    private static final Color GREEN       = new Color(61,  181, 98);

    /** --hh-green-deep: #2a9e4f */
    private static final Color GREEN_DEEP  = new Color(42,  158, 79);

    /** --hh-green-subtle: rgba(61,181,98,0.10) → solid equivalent */
    private static final Color GREEN_SUBTLE = new Color(236, 248, 240);

    /** --hh-text: #111827 */
    private static final Color TEXT        = new Color(17,  24,  39);

    /** --hh-secondary: #6b7280 */
    private static final Color SECONDARY   = new Color(107, 114, 128);

    /** --hh-muted: #9ca3af */
    private static final Color MUTED       = new Color(156, 163, 175);

    /** --hh-border: #e5e7eb */
    private static final Color BORDER      = new Color(229, 231, 235);

    /** --hh-bg: #f5f8f5 */
    private static final Color BG          = new Color(245, 248, 245);

    /** white */
    private static final Color WHITE       = Color.WHITE;

    // ── Typography ────────────────────────────────────────────────────────────

    private static Font font(int size, int style, Color color) {
        return new Font(Font.HELVETICA, size, style, color);
    }

    private static final Font F_BRAND        = font(22, Font.BOLD,   GREEN);
    private static final Font F_REPORT_TITLE = font(13, Font.NORMAL, SECONDARY);
    private static final Font F_SECTION      = font(13, Font.BOLD,   GREEN_DEEP);
    private static final Font F_META_LABEL   = font( 8, Font.NORMAL, MUTED);
    private static final Font F_META_VALUE   = font(11, Font.BOLD,   TEXT);
    private static final Font F_TABLE_HEADER = font( 9, Font.BOLD,   SECONDARY);
    private static final Font F_TABLE_BODY   = font( 9, Font.NORMAL, TEXT);
    private static final Font F_STAT_LABEL   = font( 8, Font.NORMAL, MUTED);
    private static final Font F_STAT_VALUE   = font(10, Font.BOLD,   TEXT);
    private static final Font F_RECENT_TITLE = font(10, Font.BOLD,   TEXT);
    private static final Font F_FOOTER       = font( 8, Font.NORMAL, MUTED);
    private static final Font F_DIVIDER_LABEL = font( 7, Font.BOLD,  MUTED);

    // ── Spacing constants ─────────────────────────────────────────────────────

    private static final float PAD_CELL  = 8f;
    private static final float PAD_SMALL = 5f;

    // ─────────────────────────────────────────────────────────────────────────

    private final ChartGenerator chartGenerator;

    public PdfGenerator(ChartGenerator chartGenerator) {
        this.chartGenerator = chartGenerator;
    }

    // ── Public entry point ────────────────────────────────────────────────────

    public byte[] generate(HealthReportData reportData) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document doc = new Document(PageSize.A4, 40, 40, 32, 40);

            PdfWriter writer = PdfWriter.getInstance(doc, out);
            writer.setPageEvent(new HeaderFooterEvent(reportData));

            doc.open();

            addHeader(doc, reportData);
            addDivider(doc);
            addBloodPressureSection(doc, reportData);
            addDivider(doc);
            addFastingSugarSection(doc, reportData);
            addDivider(doc);
            addPostMealSugarSection(doc, reportData);

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Unable to generate PDF.", e);
        }
    }

    // ── Page header / meta card ───────────────────────────────────────────────

    private void addHeader(Document doc, HealthReportData reportData) throws Exception {

        // Brand name
        Paragraph brand = new Paragraph("HouseHealth", F_BRAND);
        brand.setAlignment(Element.ALIGN_LEFT);
        brand.setSpacingAfter(2);
        doc.add(brand);

        Paragraph subtitle = new Paragraph("Health Summary Report", F_REPORT_TITLE);
        subtitle.setAlignment(Element.ALIGN_LEFT);
        subtitle.setSpacingAfter(20);
        doc.add(subtitle);

        // Meta row: patient, email, date
        PdfPTable meta = new PdfPTable(3);
        meta.setWidthPercentage(100);
        meta.setSpacingAfter(20);
        meta.setWidths(new float[]{1, 1.4f, 1});

        String generated = DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        addMetaCell(meta, "Patient",   reportData.getUser().getName(),  false);
        addMetaCell(meta, "Email",     reportData.getUser().getEmail(), false);
        addMetaCell(meta, "Generated", generated,                        true);

        doc.add(meta);
    }

    private void addMetaCell(PdfPTable table, String label, String value, boolean last) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0);
        cell.setPaddingRight(last ? 0 : 24);

        Paragraph lp = new Paragraph(label.toUpperCase(), F_META_LABEL);
        lp.setSpacingAfter(3);
        cell.addElement(lp);
        cell.addElement(new Paragraph(value, F_META_VALUE));

        table.addCell(cell);
    }

    // ── Horizontal rule ───────────────────────────────────────────────────────

    private void addDivider(Document doc) throws Exception {
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        line.setSpacingBefore(6);
        line.setSpacingAfter(20);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BORDER);
        cell.setBorderWidth(0.8f);
        cell.setFixedHeight(1);
        cell.setPadding(0);
        line.addCell(cell);

        doc.add(line);
    }

    // ── Section: Blood Pressure ───────────────────────────────────────────────

    private void addBloodPressureSection(Document doc, HealthReportData reportData) throws Exception {
        addSectionHeading(doc, "Blood Pressure");

        var trend = reportData.getDashboard().getBloodPressureTrend();
        var latest = reportData.getDashboard().getLatestBloodPressure();

        String latestVal   = latest.getSystolic() + " / " + latest.getDiastolic() + " mmHg";
        String currentAvg  = String.format("%.1f / %.1f mmHg", trend.getCurrentAverageSystolic(),  trend.getCurrentAverageDiastolic());
        String previousAvg = String.format("%.1f / %.1f mmHg", trend.getPreviousAverageSystolic(), trend.getPreviousAverageDiastolic());
        String change      = String.format("%.1f%%", trend.getSystolicPercentageChange());
        String trendLabel  = trend.getTrendStatus().name();

        addStatsRow(doc, latestVal, currentAvg, previousAvg, change, trendLabel);
        addChart(doc, chartGenerator.createBloodPressureChart(reportData.getRecentBloodPressureLogs()));
        addRecentReadings(doc, reportData.getRecentBloodPressureLogs(), true);
    }

    // ── Section: Fasting Sugar ────────────────────────────────────────────────

    private void addFastingSugarSection(Document doc, HealthReportData reportData) throws Exception {
        addSectionHeading(doc, "Fasting Blood Sugar");

        var trend  = reportData.getDashboard().getFastingSugarTrend();
        var latest = reportData.getDashboard().getLatestFastingSugar();

        String latestVal   = latest.getSugarValue() + " mg/dL";
        String currentAvg  = String.format("%.1f mg/dL", trend.getCurrentAverageSugar());
        String previousAvg = String.format("%.1f mg/dL", trend.getPreviousAverageSugar());
        String change      = String.format("%.1f%%", trend.getPercentageChange());
        String trendLabel  = trend.getTrendStatus().name();

        addStatsRow(doc, latestVal, currentAvg, previousAvg, change, trendLabel);
        addChart(doc, chartGenerator.createSugarChart(reportData.getRecentFastingSugarLogs()));
        addRecentReadings(doc, reportData.getRecentFastingSugarLogs(), false);
    }

    // ── Section: Post-Meal Sugar ──────────────────────────────────────────────

    private void addPostMealSugarSection(Document doc, HealthReportData reportData) throws Exception {
        addSectionHeading(doc, "Post Meal Blood Sugar");

        var trend  = reportData.getDashboard().getPostMealSugarTrend();
        var latest = reportData.getDashboard().getLatestPostMealSugar();

        String latestVal   = latest.getSugarValue() + " mg/dL";
        String currentAvg  = String.format("%.1f mg/dL", trend.getCurrentAverageSugar());
        String previousAvg = String.format("%.1f mg/dL", trend.getPreviousAverageSugar());
        String change      = String.format("%.1f%%", trend.getPercentageChange());
        String trendLabel  = trend.getTrendStatus().name();

        addStatsRow(doc, latestVal, currentAvg, previousAvg, change, trendLabel);
        addChart(doc, chartGenerator.createSugarChart(reportData.getRecentPostMealSugarLogs()));
        addRecentReadings(doc, reportData.getRecentPostMealSugarLogs(), false);

        // Footer disclaimer
        Paragraph footer = new Paragraph(
                "This report is for record-keeping purposes only and does not constitute medical advice.",
                F_FOOTER
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(28);
        doc.add(footer);
    }

    // ── Section heading ───────────────────────────────────────────────────────

    private void addSectionHeading(Document doc, String title) throws Exception {
        // Uppercase label above the title — mirrors .dashboard-label style
        Paragraph label = new Paragraph(title.toUpperCase(), F_DIVIDER_LABEL);
        label.setSpacingAfter(4);
        doc.add(label);

        // Accent bar (thin green line, 32px wide — decorative, minimal)
        PdfPTable bar = new PdfPTable(1);
        bar.setWidthPercentage(7);
        bar.setHorizontalAlignment(Element.ALIGN_LEFT);
        bar.setSpacingAfter(14);

        PdfPCell barCell = new PdfPCell();
        barCell.setBackgroundColor(GREEN);
        barCell.setBorder(Rectangle.NO_BORDER);
        barCell.setFixedHeight(3f);
        barCell.setPadding(0);
        bar.addCell(barCell);

        doc.add(bar);
    }

    // ── Stats row (4 stat cards + trend pill) ────────────────────────────────

    private void addStatsRow(
            Document doc,
            String latest,
            String currentAvg,
            String previousAvg,
            String change,
            String trend
    ) throws Exception {

        PdfPTable row = new PdfPTable(5);
        row.setWidthPercentage(100);
        row.setWidths(new float[]{1.1f, 1f, 1f, 0.8f, 1f});
        row.setSpacingAfter(14);

        addStatCard(row, "Latest",         latest);
        addStatCard(row, "Current Avg",    currentAvg);
        addStatCard(row, "Previous Avg",   previousAvg);
        addStatCard(row, "Change",         change);
        addTrendPill(row, trend);

        doc.add(row);
    }

    private void addStatCard(PdfPTable table, String label, String value) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BG);
        cell.setBorderColor(BORDER);
        cell.setBorderWidth(0.6f);
        cell.setPadding(PAD_CELL);
        cell.setPaddingRight(PAD_CELL + 4);

        Paragraph lp = new Paragraph(label.toUpperCase(), F_STAT_LABEL);
        lp.setSpacingAfter(4);
        cell.addElement(lp);
        cell.addElement(new Paragraph(value, F_STAT_VALUE));

        table.addCell(cell);
    }

    private void addTrendPill(PdfPTable table, String trend) {
        // Pick background color matching app's trend-badge classes
        Color bg;
        Color fg;
        switch (trend.toUpperCase()) {
            case "IMPROVING":
                bg = GREEN_SUBTLE;
                fg = GREEN_DEEP;
                break;
            case "WORSENING":
                bg = new Color(254, 226, 226);
                fg = new Color(185, 28, 28);
                break;
            case "STABLE":
                bg = new Color(219, 234, 254);
                fg = new Color(29, 78, 216);
                break;
            default:
                bg = new Color(243, 244, 246);
                fg = SECONDARY;
        }

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(bg);
        cell.setBorderColor(BORDER);
        cell.setBorderWidth(0.6f);
        cell.setPadding(PAD_CELL);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph lp = new Paragraph("Trend".toUpperCase(), F_STAT_LABEL);
        lp.setSpacingAfter(4);
        cell.addElement(lp);

        String display = trend.charAt(0) + trend.substring(1).toLowerCase().replace("_", " ");
        cell.addElement(new Paragraph(display, font(10, Font.BOLD, fg)));

        table.addCell(cell);
    }

    // ── Chart ─────────────────────────────────────────────────────────────────

    private void addChart(Document doc, Image chart) throws Exception {
        chart.scaleToFit(515, 100);
        chart.setAlignment(Element.ALIGN_CENTER);
        chart.setSpacingAfter(14);
        doc.add(chart);
    }

    // ── Recent readings table ─────────────────────────────────────────────────

    private void addRecentReadings(Document doc, List<HealthLog> logs, boolean isBP) throws Exception {
        Paragraph title = new Paragraph("Recent Readings", F_RECENT_TITLE);
        title.setSpacingAfter(6);
        doc.add(title);

        PdfPTable table = isBP ? new PdfPTable(3) : new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(8);

        if (isBP) {
            addTableHeader(table, "Date");
            addTableHeader(table, "Systolic (mmHg)");
            addTableHeader(table, "Diastolic (mmHg)");
        } else {
            addTableHeader(table, "Date");
            addTableHeader(table, "Value (mg/dL)");
        }

        boolean alt = false;
        for (int i = 0; i < Math.min(5, logs.size()); i++) {
            HealthLog log = logs.get(i);
            if (isBP) {
                addTableCell(table, formatDate(log),               alt);
                addTableCell(table, String.valueOf(log.getSystolic()),  alt);
                addTableCell(table, String.valueOf(log.getDiastolic()), alt);
            } else {
                addTableCell(table, formatDate(log),                  alt);
                addTableCell(table, String.valueOf(log.getSugarValue()), alt);
            }
            alt = !alt;
        }

        doc.add(table);
    }

    private void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, F_TABLE_HEADER));
        cell.setBackgroundColor(GREEN_SUBTLE);
        cell.setBorderColor(BORDER);
        cell.setBorderWidth(0.6f);
        cell.setPadding(PAD_CELL);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, boolean alternate) {
        PdfPCell cell = new PdfPCell(new Phrase(text, F_TABLE_BODY));
        cell.setBackgroundColor(alternate ? BG : WHITE);
        cell.setBorderColor(BORDER);
        cell.setBorderWidth(0.6f);
        cell.setPadding(PAD_CELL);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String formatDate(HealthLog log) {
        return DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .withZone(ZoneId.systemDefault())
                .format(log.getLoggedAt());
    }

    // ── Page event: running header line ──────────────────────────────────────

    private static class HeaderFooterEvent extends PdfPageEventHelper {

        private final HealthReportData reportData;

        HeaderFooterEvent(HealthReportData reportData) {
            this.reportData = reportData;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            float pageWidth  = document.getPageSize().getWidth();
            float bottom     = document.bottom() - 18;

            // Footer: page number centred
            cb.setColorFill(MUTED);
            try {
                cb.setFontAndSize(BaseFont.createFont(
                        BaseFont.HELVETICA, BaseFont.CP1252, false), 7);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String pageStr = "Page " + writer.getPageNumber();
            cb.beginText();
            cb.showTextAligned(
                    PdfContentByte.ALIGN_CENTER,
                    pageStr,
                    pageWidth / 2,
                    bottom,
                    0
            );
            cb.endText();

            // Thin top border on every page after page 1
            if (writer.getPageNumber() > 1) {
                cb.setColorStroke(BORDER);
                cb.setLineWidth(0.5f);
                cb.moveTo(document.left(), document.top() + 10);
                cb.lineTo(document.right(), document.top() + 10);
                cb.stroke();
            }
        }
    }
}