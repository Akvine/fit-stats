package ru.akvine.fitstats.controllers.rest.converter.parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.admin.file.ProductXlsxRow;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.exceptions.util.ExcelException;

import java.util.ArrayList;
import java.util.List;

@Component
public class XlsxParser implements Parser {
    @Override
    public List<?> parse(MultipartFile file, Class clazz) {
        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<ProductXlsxRow> records = new ArrayList<>();

            for (Row row : sheet) {
                ProductXlsxRow productXlsxRow = new ProductXlsxRow();

                while(row.iterator().hasNext()) {
                    productXlsxRow.setRowNumber(row.getCell(0).getStringCellValue());
                    productXlsxRow.setUuid(row.getCell(1).getStringCellValue());
                    productXlsxRow.setTitle(row.getCell(2).getStringCellValue());
                    productXlsxRow.setProducer(row.getCell(3).getStringCellValue());
                    productXlsxRow.setProteins(row.getCell(4).getStringCellValue());
                    productXlsxRow.setFats(row.getCell(5).getStringCellValue());
                    productXlsxRow.setCarbohydrates(row.getCell(6).getStringCellValue());
                    productXlsxRow.setCalories(row.getCell(7).getStringCellValue());
                    productXlsxRow.setAlcohol(row.getCell(8).getStringCellValue());
                    productXlsxRow.setVol(row.getCell(9).getStringCellValue());
                    productXlsxRow.setVolume(row.getCell(10).getStringCellValue());
                    productXlsxRow.setMeasurement(row.getCell(11).getStringCellValue());
                }

                records.add(productXlsxRow);
            }

            return records;
        } catch (Exception e) {
            throw new ExcelException("Can't parse file, ex = " + e.getMessage());
        }
    }

    @Override
    public ConverterType getType() {
        return ConverterType.XLSX;
    }
}
