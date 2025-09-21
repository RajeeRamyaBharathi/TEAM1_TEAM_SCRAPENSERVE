package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.*;

public class ExcelReader {

    private List<String> eliminate = new ArrayList<>();
    private List<String> toAdd = new ArrayList<>();

    /**
     * Constructor reads elimination and addition terms from an Excel file.
     *
     * @param filePath     Path to the Excel file
     * @param sheetIndex   Index of the sheet to read from
     * @param eliminateCol Column index for eliminate terms
     * @param addCol       Column index for add terms
     */
    public ExcelReader(String filePath, int sheetIndex, int eliminateCol, int addCol) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(sheetIndex);
            LoggerLoad.info("Reading Excel sheet: " + sheet.getSheetName());

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                String elim = getCellValue(row, eliminateCol);
                String add = getCellValue(row, addCol);

                if (!elim.isEmpty()) {
                    eliminate.add(elim.toLowerCase());
                    LoggerLoad.debug("Loaded eliminate term: " + elim);
                }
                if (!add.isEmpty()) {
                    toAdd.add(add.toLowerCase());
                    LoggerLoad.debug("Loaded add term: " + add);
                }
            }

            LoggerLoad.info("Loaded eliminate: " + eliminate.size() + ", add: " + toAdd.size());

        } catch (Exception e) {
            LoggerLoad.error("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        return (cell == null) ? "" : cell.toString().trim().toLowerCase();
    }

    public List<String> getMatchedEliminate(String ingredients) {
        return matchFromList(ingredients, eliminate);
    }

    public List<String> getMatchedAdd(String ingredients) {
        return matchFromList(ingredients, toAdd);
    }

    /**
     * Safely matches terms from the list against input text.
     *
     * @param text  The input text (e.g., ingredients)
     * @param list  List of words to match
     * @return List of matched terms
     */
    private List<String> matchFromList(String text, List<String> list) {
        List<String> matches = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            LoggerLoad.warn("Input text is null or empty in matchFromList.");
            return matches; // Return empty list if input is null or blank
        }

        text = text.toLowerCase();
        LoggerLoad.debug("Normalized ingredient text for matching: " + text);
        for (String item : list) {
            String escaped = Pattern.quote(item);

            // word boundary matching, allows plurals
            Pattern p = Pattern.compile("\\b" + escaped + "s?\\b");
            Matcher m = p.matcher(text);

            if (m.find()) {
                matches.add(item);
                LoggerLoad.debug("Matched item: " + item);
            }
        }


        return matches;
    }
}
