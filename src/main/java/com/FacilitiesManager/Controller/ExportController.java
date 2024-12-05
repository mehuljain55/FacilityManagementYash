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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportController {

    @PostMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestBody List<CabinRequest> bookingRequests) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cabin Requests");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Request ID", "Cabin ID","Cabin Name", "User ID", "Purpose", "Office Location",
                "Valid From", "Valid Till", "Start Date", "End Date",
                "Booking Validity", "Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderCellStyle(workbook));
        }

        int rowNum = 1;
        for (CabinRequest request : bookingRequests) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(request.getRequestId());
            row.createCell(1).setCellValue(request.getCabinId());
            row.createCell(2).setCellValue(request.getCabinName());
            row.createCell(3).setCellValue(request.getUserId());
            row.createCell(4).setCellValue(request.getPurpose());
            row.createCell(5).setCellValue(request.getOfficeId());
            row.createCell(6).setCellValue(convertTime(request.getValidFrom()));
            row.createCell(7).setCellValue(convertTime(request.getValidTill()));
            row.createCell(8).setCellValue(convertDate(request.getStartDate()));
            row.createCell(9).setCellValue(convertDate(request.getEndDate()));
            row.createCell(10).setCellValue(request.getBookingValadity().toString());
            row.createCell(11).setCellValue(request.getStatus().toString());
        }


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

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

            Row header = sheet.createRow(0);
            String[] columns = {"Booking ID", "Cabin ID","Cabin Name", "User ID", "Purpose", "Office ID",
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
                row.createCell(2).setCellValue(booking.getCabinName());
                row.createCell(3).setCellValue(booking.getUserId());
                row.createCell(4).setCellValue(booking.getPurpose());
                row.createCell(5).setCellValue(booking.getOfficeId());
                row.createCell(6).setCellValue(convertTime(booking.getValidFrom()));
                row.createCell(7).setCellValue(convertTime(booking.getValidTill()));
                row.createCell(8).setCellValue(convertDate(booking.getStartDate()));
                row.createCell(9).setCellValue(convertDate(booking.getEndDate()));
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

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

    private  String convertTime(LocalTime time)
    {
        if (time == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a"); // 12-hour format with AM/PM
        return time.format(formatter);
    }
}
