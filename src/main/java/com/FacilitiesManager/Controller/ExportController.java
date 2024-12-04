package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.BookingModel;
import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.CabinRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportController {

    @PostMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestBody List<CabinRequest> bookingRequests) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cabin Requests");

        // Add header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Request ID", "Cabin ID", "User ID", "Purpose", "Office ID",
                "Valid From", "Valid Till", "Start Date", "End Date",
                "Booking Validity", "Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderCellStyle(workbook));
        }

        // Populate data rows
        int rowNum = 1;
        for (CabinRequest request : bookingRequests) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(request.getRequestId());
            row.createCell(1).setCellValue(request.getCabinId());
            row.createCell(2).setCellValue(request.getUserId());
            row.createCell(3).setCellValue(request.getPurpose());
            row.createCell(4).setCellValue(request.getOfficeId());
            row.createCell(5).setCellValue(request.getValidFrom().toString());
            row.createCell(6).setCellValue(request.getValidTill().toString());
            row.createCell(7).setCellValue(convertDate(request.getStartDate()));
            row.createCell(8).setCellValue(convertDate(request.getEndDate()));
            row.createCell(9).setCellValue(request.getBookingValadity().toString());
            row.createCell(10).setCellValue(request.getStatus().toString());
        }

        // Auto-size all columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i); // Adjusts the column width to fit the content
        }

        // Write workbook to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] excelData = outputStream.toByteArray();

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add("Content-Disposition", "attachment; filename=cabin_requests.xlsx");

        return new ResponseEntity<>(excelData, headersResponse, HttpStatus.OK);
    }

    @PostMapping("/bookings")
    public ResponseEntity<byte[]> exportBookingData(@RequestBody List<Bookings> bookings) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Bookings");

            // Add headers
            Row header = sheet.createRow(0);
            String[] columns = {"Booking ID", "Cabin ID", "User ID", "Purpose", "Office ID",
                    "Valid From", "Valid Till", "Start Date", "End Date"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (Bookings booking : bookings) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(booking.getBookingId());
                row.createCell(1).setCellValue(booking.getCabinId());
                row.createCell(2).setCellValue(booking.getUserId());
                row.createCell(3).setCellValue(booking.getPurpose());
                row.createCell(4).setCellValue(booking.getOfficeId());
                row.createCell(5).setCellValue(booking.getValidFrom().toString());
                row.createCell(6).setCellValue(booking.getValidTill().toString());
                row.createCell(7).setCellValue(convertDate(booking.getStartDate()));
                row.createCell(8).setCellValue(convertDate(booking.getEndDate()));
            }

            // Adjust column widths
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write workbook to byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("Bookings.xlsx")
                    .build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER); // Center-align headers
        return style;
    }

    private String convertDate(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}
