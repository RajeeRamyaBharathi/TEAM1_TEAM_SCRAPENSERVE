package Utilities;

//This class  reads different filter keywords (Eliminate, Add, Avoid, Process) from an Excel sheet using Apache POI. It stores them in lists, and later matches these keywords against recipe ingredients using regex to classify recipes

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
    private List<String> avoid = new ArrayList<>();
    private List<String> process = new ArrayList<>();
    //Constructor for Eliminate + Add only
    public ExcelReader(String filePath, int sheetIndex, int eliminateCol, int addCol) {
        this(filePath, sheetIndex, eliminateCol, addCol, -1, -1); 
    }
    //supports eliminate, add, avoid, process
    public ExcelReader(String filePath, int sheetIndex,int eliminateCol, int addCol, int avoidCol, int processCol) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            LoggerLoad.info("Reading Excel sheet: " + sheet.getSheetName());
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; 
                if (eliminateCol >= 0) {
                    String elim = getCellValue(row, eliminateCol);
                    if (!elim.isEmpty()) eliminate.add(elim.toLowerCase());
                }
                if (addCol >= 0) {
                    String add = getCellValue(row, addCol);
                    if (!add.isEmpty()) toAdd.add(add.toLowerCase());
                }
                if (avoidCol >= 0) {
                    String av = getCellValue(row, avoidCol);
                    if (!av.isEmpty()) avoid.add(av.toLowerCase());
                }
                if (processCol >= 0) {
                    String pr = getCellValue(row, processCol);
                    if (!pr.isEmpty()) process.add(pr.toLowerCase());
                }
            }

            LoggerLoad.info("Loaded terms → eliminate: " + eliminate.size()
                    + ", add: " + toAdd.size()
                    + ", avoid: " + avoid.size()
                    + ", process: " + process.size());

        } catch (Exception e) {
            LoggerLoad.error("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getCellValue(Row row, int colIndex) {
        if (colIndex < 0) return "";
        Cell cell = row.getCell(colIndex);
        return (cell == null) ? "" : cell.toString().trim().toLowerCase();
    }
    //Public getters
    public List<String> getMatchedEliminate(String ingredients) {
        return matchFromList(ingredients, eliminate);
    }

    public List<String> getMatchedAdd(String ingredients) {
        return matchFromList(ingredients, toAdd);
    }

    public List<String> getMatchedAvoid(String ingredients) {
        return matchFromList(ingredients, avoid);
    }

    public List<String> getMatchedProcess(String ingredients) {
        return matchFromList(ingredients, process);
    }
    //Match helper
    private List<String> matchFromList(String text, List<String> list) {
        List<String> matches = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            LoggerLoad.warn("Input text is null or empty in matchFromList.");
            return matches;
        }
        text = text.toLowerCase();
        for (String item : list) {
            String escaped = Pattern.quote(item);
            Pattern p = Pattern.compile("\\b" + escaped + "s?\\b");
            Matcher m = p.matcher(text);
            if (m.find()) {
                matches.add(item);
            }
        }
        return matches;
    }
}
