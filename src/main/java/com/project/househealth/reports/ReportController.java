package com.project.househealth.reports;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final PdfReportService pdfReportService;

    public ReportController(
            PdfReportService pdfReportService
    ) {
        this.pdfReportService = pdfReportService;
    }

    @GetMapping(
            value = "/health",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> exportHealthReport() {

        byte[] pdf =
                pdfReportService.generateHealthReport();

        String fileName =
                "HouseHealth_Report_"
                        + LocalDate.now()
                        + ".pdf";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(fileName)
                                .build()
                                .toString()
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }
}