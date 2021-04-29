import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class DisplayerExcel extends Displayer{


    public DisplayerExcel(String outputDir, String fileName) {
        super(outputDir, fileName);
    }

    @Override
    public void display(Collection<People> _allpeople, String _fileName, String _courseName, String _startTime, String _endTime) {

        Workbook wb = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) wb.createSheet(_courseName);

        LinkedList<Object> titleLine = new LinkedList<>();
        titleLine.add("Rapport de connexion");

        LinkedList<Object> dateLine = new LinkedList<>();
        dateLine.add("Date");
        dateLine.add(LocalDate.now());

        LinkedList<Object> startTimeLine = new LinkedList<>();
        startTimeLine.add("Heure début");
        startTimeLine.add(_startTime);

        LinkedList<Object> endTimeLine = new LinkedList<>();
        endTimeLine.add("Heure fin");
        endTimeLine.add(_endTime);

        LinkedList<Object> courseNameLine = new LinkedList<>();
        courseNameLine.add("Cours");
        courseNameLine.add(_courseName);

        LinkedList<Object> fileNameLine = new LinkedList<>();
        fileNameLine.add("Fichier analysé");
        fileNameLine.add(_fileName);

        LinkedList<Object> numberOfStudentsLine = new LinkedList<>();
        numberOfStudentsLine.add("Nombre de connectés");
        numberOfStudentsLine.add(_allpeople.size());

        LinkedList<Object> emptyLine = new LinkedList<>();

        List<String> peopleData = People.getExtractor().getData();
        LinkedList<Object> headerLine = new LinkedList<>();
        if (peopleData.contains("id"))
            headerLine.add("Réference");
        if (peopleData.contains("name"))
            headerLine.add("Nom");
        if (peopleData.contains("time")) {
            headerLine.add("Durée");
            headerLine.add("Pourcentage");
        }

        LinkedList<LinkedList<Object>> data = new LinkedList<LinkedList<Object>>() {
            {
                add(titleLine);
                add(dateLine);
                add(startTimeLine);
                add(endTimeLine);
                add(courseNameLine);
                add(fileNameLine);
                add(numberOfStudentsLine);
                add(emptyLine);
                add(headerLine);
            }
        };

        Iterator<People> pIterator = _allpeople.iterator();
        while (pIterator.hasNext()) {
            People people = pIterator.next();

            if (people.isOutOfPeriod()) {
                continue;
            }

            LinkedList<Object> peopleLine = new LinkedList<>();

            if (peopleData.contains("id")) {
                peopleLine.add(people.get_id());
            }

            if (peopleData.contains("name")) {
                peopleLine.add(people.getName());
            }

            if (peopleData.contains("time")) {
                Long duration = people.getTotalAttendanceDuration();
                peopleLine.add(duration);

                LocalDateTime startTime = TEAMSDateTimeConverter.StringToLocalDateTime(_startTime);
                LocalDateTime endTime = TEAMSDateTimeConverter.StringToLocalDateTime(_endTime);
                double durationMaxMinutes = Math.abs(Duration.between(startTime, endTime).toSeconds()/60.);
                Double percentage = (duration/durationMaxMinutes);
                peopleLine.add(percentage);
            }

            data.add(peopleLine);
        }

        int rowCount = 0;
        int columnCount = 0;
        Iterator<LinkedList<Object>> lineIterator = data.iterator();
        while (lineIterator.hasNext()) {
            LinkedList<Object> line = lineIterator.next();
            Row row = sheet.createRow(rowCount++);
            columnCount = 0;
            Iterator<Object> fieldIterator = line.iterator();
            while (fieldIterator.hasNext()) {
                Object field = fieldIterator.next();
                Cell cell = row.createCell(columnCount++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof LocalDate) {
                    CellStyle cellStyle = wb.createCellStyle();
                    CreationHelper createHelper = wb.getCreationHelper();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
                    cell.setCellValue(Date.valueOf((LocalDate) field));
                    cell.setCellStyle(cellStyle);
                } else if (field instanceof Long) {
                    cell.setCellValue((Long) field);
                } else if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                    CellStyle stylePercentage = wb.createCellStyle();
                    stylePercentage.setDataFormat(wb.createDataFormat()
                            .getFormat(BuiltinFormats.getBuiltinFormat( 9 ))); // Formatage en pourcentage
                    cell.setCellStyle(stylePercentage);
                }
            }
        }

        for (int i = 0; i < Math.max(columnCount, 2); i++) {
            sheet.autoSizeColumn(i);
        }

        String filePath = outputDir + "/" + fileName + ".xlsx";
        File excelFile = new File(filePath);
        try {
            Files.createDirectories(Paths.get(outputDir + "/"));
            excelFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(filePath);
            wb.write(outputStream);
            Desktop.getDesktop().open(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
