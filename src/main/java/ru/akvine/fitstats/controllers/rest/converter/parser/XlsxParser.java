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
    private static final int ROW_NUMBER_INDEX = 0;
    private static final int UUID_INDEX = 1;
    private static final int TITLE_INDEX = 2;
    private static final int PRODUCER_INDEX = 3;
    private static final int PROTEINS_INDEX = 4;
    private static final int FATS_INDEX = 5;
    private static final int CARBOHYDRATES_INDEX = 6;
    private static final int CALORIES_INDEX = 7;
    private static final int ALCOHOL_INDEX = 8;
    private static final int VOL_INDEX = 9;
    private static final int VOLUME_INDEX = 10;
    private static final int VOLUME_MEASUREMENT_INDEX = 11;

    @Override
    public List<?> parse(MultipartFile file, Class clazz) {
        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<ProductXlsxRow> records = new ArrayList<>();

            for (Row row : sheet) {
                ProductXlsxRow productXlsxRow = new ProductXlsxRow();

                while(row.iterator().hasNext()) {
                    productXlsxRow.setRowNumber(row.getCell(ROW_NUMBER_INDEX).getStringCellValue());
                    productXlsxRow.setUuid(row.getCell(UUID_INDEX).getStringCellValue());
                    productXlsxRow.setTitle(row.getCell(TITLE_INDEX).getStringCellValue());
                    productXlsxRow.setProducer(row.getCell(PRODUCER_INDEX).getStringCellValue());
                    productXlsxRow.setProteins(row.getCell(PROTEINS_INDEX).getStringCellValue());
                    productXlsxRow.setFats(row.getCell(FATS_INDEX).getStringCellValue());
                    productXlsxRow.setCarbohydrates(row.getCell(CARBOHYDRATES_INDEX).getStringCellValue());
                    productXlsxRow.setCalories(row.getCell(CALORIES_INDEX).getStringCellValue());
                    productXlsxRow.setAlcohol(row.getCell(ALCOHOL_INDEX).getStringCellValue());
                    productXlsxRow.setVol(row.getCell(VOL_INDEX).getStringCellValue());
                    productXlsxRow.setVolume(row.getCell(VOLUME_INDEX).getStringCellValue());
                    productXlsxRow.setMeasurement(row.getCell(VOLUME_MEASUREMENT_INDEX).getStringCellValue());
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
