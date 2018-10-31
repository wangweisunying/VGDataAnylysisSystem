/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Wei Wang
 */
public class ExcelOperation {

    public static enum ExcelType {
        XLS,
        XLSX
    }

    public static Workbook getReadConnection(String path, ExcelType type) throws IOException {
        FileInputStream fileIn = new FileInputStream(path);
        Workbook wb;
        switch (type) {
            case XLS:
                wb = new HSSFWorkbook(fileIn);
                break;
            case XLSX:
                wb = new XSSFWorkbook(fileIn);
                break;
            default:
                wb = null;
                System.out.println("excel format input is wrong!!");
                break;
        }
        return wb;
    }

    public static Workbook getWriteConnection(ExcelType type) throws IOException {
        Workbook wb;
        switch (type) {
            case XLS:
                wb = new HSSFWorkbook();
                break;
            case XLSX:
                wb = new XSSFWorkbook();
                break;
            default:
                wb = null;
                System.out.println("excel format input is wrong!!");
                break;
        }
        return wb;

    }

    public static void writeExcel(String path ,Workbook wb ) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("Your excel file has been generated!");
    }
    
    public static String transferIntgerToString(int n) throws Exception{
        if(n < 1){
            throw new Exception("n can not be less than 1!");
        }
        // write from bottom 
        String res = "";
        while( n > 26){
            res = (char)(n % 26 + 64) + res;
            n = n / 26;
        }
        res = (char)(n + 64) + res;
        return res;
    }
    
    public static void setConditionalFormatting(Sheet sheet , IndexedColors color , byte compare , String[] thresholdArr , String cellRange) throws Exception{
        if(thresholdArr == null || thresholdArr.length == 0 || thresholdArr.length > 2){
            throw new Exception("thresholdArr must be either 1 or 2!");
        }
        if(compare == ComparisonOperator.BETWEEN && thresholdArr.length == 1){
            throw new Exception("Between operations need two parameters!");
        }
        if(compare != ComparisonOperator.BETWEEN && thresholdArr.length == 2){
            throw new Exception("greater or lessthan operations need one parameters!");
        }
        if(!cellRange.matches("[A-Z]+\\d+:[A-Z]+\\d+")){
            throw new Exception("Wrong format input of the Cell Range");
        }
        if(thresholdArr.length == 2 && Integer.parseInt(thresholdArr[0]) >= Integer.parseInt(thresholdArr[1])){
            throw new Exception("Less first please!");
        }
        
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule;
        if(compare == ComparisonOperator.BETWEEN){
            rule = sheetCF.createConditionalFormattingRule(compare, thresholdArr[0] , thresholdArr[1]);
        }else{
            rule = sheetCF.createConditionalFormattingRule(compare, thresholdArr[0]);
        }
        
        PatternFormatting fill1 = rule.createPatternFormatting();
        fill1.setFillBackgroundColor(color.index);
        
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf(cellRange)
        };
        sheetCF.addConditionalFormatting(regions, rule);
    }

}
