package ru.akvine.fitstats.services.processors.format;

import com.google.common.base.Preconditions;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.exceptions.util.ExcelException;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static ru.akvine.fitstats.utils.ReflectionUtils.getFieldNames;
import static ru.akvine.fitstats.utils.ReflectionUtils.getFieldsValues;

@Component
public class XlsxConverter implements Converter {
    @Override
    public <T> byte[] convert(List<T> records, Class<T> clazz) {
        Preconditions.checkNotNull(records, "records is null");
        Preconditions.checkNotNull(clazz, "class is null");

        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Records");
            createHeader(sheet, clazz);

            int rowNumber = 1;
            for (int i = 0; i < records.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<String> fieldsValues = getFieldsValues(records.get(i));
                Cell rowNumberCell = row.createCell(0);

                rowNumberCell.setCellValue(rowNumber);
                rowNumber++;

                for (int j = 0; j < fieldsValues.size(); j++) {
                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(fieldsValues.get(j));
                }
            }

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new ExcelException("Fail convert records to excel, ex = " + e.getMessage());
        }
    }

    @Override
    public ConverterType getType() {
        return ConverterType.XLSX;
    }

    private <T> void createHeader(Sheet sheet, Class<T> clazz) {
        List<String> fieldsNames = getFieldNames(clazz);
        Row row = sheet.createRow(0);

        Cell rowNumberCell = row.createCell(0);
        rowNumberCell.setCellValue("rowNumber");

        for (int i = 0; i < fieldsNames.size(); ++i) {
            Cell cell = row.createCell(i + 1);
            cell.setCellValue(fieldsNames.get(i));
        }
    }
}
