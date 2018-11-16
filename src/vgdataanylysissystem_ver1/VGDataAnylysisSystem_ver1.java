/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgdataanylysissystem_ver1;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.mail.MessagingException;
import model.DataBaseCon;
import model.EmailAndText;
import model.ExcelOperation;
import model.LXDataBaseCon;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import panels.Panel;
import panels.PanelCardiology;
import panels.PanelCorn;
import panels.PanelCreatinine;
import panels.PanelDairy;
import panels.PanelDiabete;
import panels.PanelEgg;
import panels.PanelHormonal;
import panels.PanelLectin;
import panels.PanelLiver;
import panels.PanelMicronutrientsV1;
import panels.PanelMicronutrientsV2;
import panels.PanelMicronutrientsV3;
import panels.PanelNut;
import panels.PanelPeanut;
import panels.PanelSeaFood;
import panels.PanelSoy;
import panels.PanelThyroid;
import panels.PanelWheatZoomer;

/**
 *
 * @author Wei Wang
 */
public class VGDataAnylysisSystem_ver1 {

    private static TestPanel[] testList = {TestPanel.THYROID,TestPanel.MICRONUTRIENTS};
    private String path = "C:\\Users\\Wei Wang\\Desktop\\VGANAlysis\\testOutPut\\";
    private String email = "thushanis@vibrantgenomics.com";
    private String[] emailCC = {"hari@vibrantsci.com" ,"shirley@vibrantgenomics.com","wei@vibrantsci.com"};
    

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */

    private enum TestPanel {
        CORN,
        DAIRY,
        EGG,
        HORMONAL,
        LECTIN,
        NUT,
        PEANUT,
        SEAFOOD,
        SOY,
        THYROID,
        LIVER,
        MICRONUTRIENTS,
        CARDIOLOGY,
        WHEAT,
        CREATININE,
        DIABETES
    }

    private class OutPutUnit {

        private List<Double> DataList;
        private int patient_id, sample_id, Age, julienBarcode;
        private String gender, height, weight, sampleCollectionTime;
        private List<String> ICDList;
        private int mapNumber;
        
        private OutPutUnit(int patient_id, int sample_id, int Age, String gender,
                String height, String weight, String sampleCollectionTime,
                List<Double> DataList, int julienBarcode, List<String> ICDList) {
            this.patient_id = patient_id;
            this.sample_id = sample_id;
            this.Age = Age;
            this.gender = gender;
            this.height = height;
            this.weight = weight;
            this.sampleCollectionTime = sampleCollectionTime;
            this.DataList = DataList;
            this.julienBarcode = julienBarcode;
            this.ICDList = ICDList;
        }
    }

    private List<String> titleList = new ArrayList();
    private Map<String, double[]> throidRefMap;
    private Map<Integer, List<String[]>> SymMap = new HashMap();
    private Map<String, Integer> titleColMap;
    private int initialColumn = 12;
    private boolean[] testCode = new boolean[2]; // {is Tyriod , isMicro}
    private LinkedHashMap<String ,Map<Integer, Integer>> mircroNutrientstitleMap;
    private List<Map<Integer, List<String[]>>> symMapList;
    
    public static void main(String[] args) throws SQLException, IOException, MessagingException {
        VGDataAnylysisSystem_ver1 test = new VGDataAnylysisSystem_ver1();
        List<Panel> panelList = test.convertToList(testList);
        Map<Integer, List<OutPutUnit>> dataMap = test.combineData(panelList);
        
        
   
        
        test.exportToExcel(dataMap);
        test.sendEmail();

    }
 
    private void sendEmail() throws MessagingException {
        StringBuilder sb = new StringBuilder();
        for (TestPanel x : testList) {
            sb.append(x.toString() + "vs");
        }
        sb.setLength(sb.length() - 2);
         
        EmailAndText.sendEmail("wei_vg@vibrantgenomics.com", "vibrant@2014", email, emailCC, "VG Test Report Auto Mail--- Please do not reply", sb.toString(), path);
    }

    private List<Panel> convertToList(TestPanel[] testList) {
        List<Panel> res = new ArrayList();
        for (TestPanel test : testList) {
            switch (test) {
                case CORN:
                    path += "CORN";
                    res.add(new PanelCorn());
                    break;
                case DAIRY:
                    path += "DAIRY";
                    res.add(new PanelDairy());
                    break;
                case EGG:
                    path += "EGG";
                    res.add(new PanelEgg());
                    break;
                case HORMONAL:
                    path += "HORMONAL";
                    res.add(new PanelHormonal());
                    break;
                case LECTIN:
                    path += "LECTIN";
                    res.add(new PanelLectin());
                    break;
                case NUT:
                    path += "NUT";
                    res.add(new PanelNut());
                    break;
                case PEANUT:
                    path += "PEANUT";
                    res.add(new PanelPeanut());
                    break;
                case SEAFOOD:
                    path += "SEAFOOD";
                    res.add(new PanelSeaFood());
                    break;
                case SOY:
                    path += "SOY";
                    res.add(new PanelSoy());
                    break;
                case THYROID:
                    path += "THYROID";
                    Panel p = new PanelThyroid();
                    throidRefMap = ((PanelThyroid) p).getRefMap();
                    res.add(p);
                    testCode[0] = true;
                    break;
                case LIVER:
                    path += "LIVER";
                    res.add(new PanelLiver());
                    break;
                case MICRONUTRIENTS:
                    testCode[1] = true;
                    path += "MICRONUTRIENTS";
                    break;
                case CARDIOLOGY:
                    path += "CARDIOLOGY";
                    res.add(new PanelCardiology());
                    break;
                case WHEAT:
                    path += "WHEAT";
                    res.add(new PanelWheatZoomer());
                    break;
                case CREATININE:
                    path += "CREATININE";
                    res.add(new PanelCreatinine());
                    break;
                case DIABETES:
                    path += "DIABETES";
                    res.add(new PanelDiabete());
                    break;
//                   
//                    MICRONUTRIENTS,
//        CARDIOLOGY,
//        WHEAT

            }
        }
        path+=".xlsx";
        return res;
    }

    

    private void exportToExcel(Map<Integer, List<OutPutUnit>> dataMap) throws IOException {
        Workbook wb = ExcelOperation.getWriteConnection(ExcelOperation.ExcelType.SXSSF);
        if (dataMap.isEmpty()) {
            System.out.println("There tests do not share patient!!!");
            return;
        }
        if(testCode[1]){
            for (int visit : dataMap.keySet()) {
                Sheet sheet = wb.createSheet("visit_" + visit);
                int rowCt = 0;
                Row row = sheet.createRow(rowCt++);
                int colCt = 0;
                
                while(colCt < initialColumn){
                    row.createCell(colCt).setCellValue(titleList.get(colCt++));
                }
                
                for(String title : mircroNutrientstitleMap.keySet()){
                    row.createCell(colCt++).setCellValue(title);
                }
                for (OutPutUnit unit : dataMap.get(visit)) {
                    row = sheet.createRow(rowCt++);
                    colCt = 0;
                    row.createCell(colCt++).setCellValue(unit.patient_id);
                    row.createCell(colCt++).setCellValue(unit.sample_id);
                    row.createCell(colCt++).setCellValue(unit.Age);
                    row.createCell(colCt++).setCellValue(unit.gender);
                    row.createCell(colCt++).setCellValue(unit.height);
                    row.createCell(colCt++).setCellValue(unit.weight);
                    row.createCell(colCt++).setCellValue(unit.sampleCollectionTime);

                    for (String icd : unit.ICDList) {
                        row.createCell(colCt++).setCellValue(icd);
                    }
                    
                    
                    
                    for(int i = 0 ; i < mircroNutrientstitleMap.keySet().size() ; i++){
                        String curTitle = sheet.getRow(0).getCell(colCt++).getStringCellValue();
                        if(!mircroNutrientstitleMap.get(curTitle).containsKey(unit.mapNumber)){
                            continue;
                        }
                        int offset = mircroNutrientstitleMap.get(curTitle).get(unit.mapNumber);
                        
//                        System.out.println(unit.mapNumber + " ");
//                        System.out.println(unit.DataList.size() + "  " + offset);
//                 
                        row.createCell(colCt - 1).setCellValue(unit.DataList.get(offset));
//            
                    }
                 
                }

            }
            
        }
        else{
            for (int visit : dataMap.keySet()) {

                Sheet sheet = wb.createSheet("visit_" + visit);
                int rowCt = 0;
                Row row = sheet.createRow(rowCt++);
                int colCt = 0;

                for (String title : titleList) {
                    row.createCell(colCt++).setCellValue(title);
//                sheet.autoSizeColumn(colCt++);
                }

//            for (String sympTitle : symMap.keySet()){
//                row.createCell(colCt).setCellValue(sympTitle);
//                symMap.put(sympTitle, colCt++);
////                sheet.autoSizeColumn(colCt++);
//            }
                for (OutPutUnit unit : dataMap.get(visit)) {
                    row = sheet.createRow(rowCt++);
                    colCt = 0;
                    row.createCell(colCt++).setCellValue(unit.patient_id);
                    row.createCell(colCt++).setCellValue(unit.sample_id);
                    row.createCell(colCt++).setCellValue(unit.Age);
                    row.createCell(colCt++).setCellValue(unit.gender);
                    row.createCell(colCt++).setCellValue(unit.height);
                    row.createCell(colCt++).setCellValue(unit.weight);
                    row.createCell(colCt++).setCellValue(unit.sampleCollectionTime);

                    for (String icd : unit.ICDList) {
                        row.createCell(colCt++).setCellValue(icd);
                    }
                    for (double x : unit.DataList) {
                        row.createCell(colCt++).setCellValue(x);
                    }

                    if (SymMap.containsKey(unit.julienBarcode)) {
                        for (String[] sym : SymMap.get(unit.julienBarcode)) {
                            row.createCell(titleColMap.get(sym[0])).setCellValue(sym[1]);
                        }

                    }

                }

            }
        }
        ExcelOperation.writeExcel(path, wb);
        wb.close();
    }
    
    private Map<Integer, List<OutPutUnit>> combineData(List<Panel> panelList) throws SQLException{
        if(!testCode[1]){
            return getData(panelList , getRefMap(panelList));
        }
        else{
            List<Map<Integer ,List<OutPutUnit>>> DataLists = new ArrayList();
            List<List<String>> titleLists = new ArrayList();
            symMapList = new ArrayList();
            
            
            titleList.clear();
            SymMap.clear();
            panelList.add(new PanelMicronutrientsV3());
            DataLists.add(getData(panelList , getRefMap(panelList)));
            titleLists.add(new ArrayList(titleList));
            symMapList.add(deepCopy(SymMap));
            panelList.remove(panelList.size() - 1);
            

            SymMap.clear();
            titleList.clear();
            panelList.add(new PanelMicronutrientsV2());
            DataLists.add(getData(panelList , getRefMap(panelList)));
            titleLists.add(new ArrayList(titleList));
            symMapList.add(deepCopy(SymMap));
            panelList.remove(panelList.size() - 1);
            

            SymMap.clear();
            titleList.clear();
            panelList.add(new PanelMicronutrientsV1());
            DataLists.add(getData(panelList , getRefMap(panelList)));
            titleLists.add(new ArrayList(titleList));
            symMapList.add(deepCopy(SymMap));
            panelList.remove(panelList.size() - 1);

            return combineShardingData(DataLists , titleLists );
        }
    }
    
    private Map<Integer, List<OutPutUnit>> combineShardingData(List<Map<Integer, List<OutPutUnit>>> DataLists, List<List<String>> titleLists){
        mircroNutrientstitleMap = new LinkedHashMap();
        for(int i = 1 ; i <= titleLists.size() ; i++){
//            System.out.println(titleLists.get(i - 1));
            // titleList 应该减去 symtmton size 
//           System.out.println(symMapList.get(i - 1).values().size());

//            System.out.println(titleLists.get(i - 1).size());
            for(int j = initialColumn ; j < titleLists.get(i - 1).size() ; j++){
                
                String curTitle = titleLists.get(i - 1).get(j);
                curTitle = curTitle.replace("_V3" , "");
                curTitle = curTitle.replace("WBC2" , "WBC");
                curTitle = curTitle.replace("RBC2" , "RBC");
                curTitle = curTitle.replace("PLASMA" , "SERUM");
                if(mircroNutrientstitleMap.containsKey(curTitle)){
                    mircroNutrientstitleMap.get(curTitle).put(4 - i , j - initialColumn );
                }
                else{
                    HashMap<Integer , Integer> tmpMap = new HashMap();
                    tmpMap.put(4 - i , j - initialColumn );
                    mircroNutrientstitleMap.put(curTitle,tmpMap);
                }
                
                
//                mircroNutrientstitleMap.computeIfAbsent(curTitle, x -> new HashMap()).put(4 - i , j - initialColumn); // 代表该title对应 几号 map 的 第几个 index
            }
        }
//        System.out.println(mircroNutrientstitleMap.size());
//        for(String title : mircroNutrientstitleMap.keySet()){
//            
//            System.out.println(mircroNutrientstitleMap.get(title));
//            
//        }
        
        
        
        
        //patientId;
        Map<Integer, OutPutUnit> wholeMap = new HashMap();
        for(int i = 0 ; i < DataLists.size() ; i++){
            int mapNumber = 3 - i; 
            for(List<OutPutUnit> list : DataLists.get(i).values()){
                for(OutPutUnit curUnit : list){
//                    List<Double> dataList = new ArrayList();
//                    OutPutUnit now = new OutPutUnit(curUnit.patient_id ,curUnit.sample_id , curUnit.Age,curUnit.gender,curUnit.height,curUnit.weight,curUnit.sampleCollectionTime,
//                                          curUnit.DataList, curUnit.julienBarcode ,curUnit.ICDList);
                    curUnit.mapNumber = mapNumber;
                    wholeMap.put(curUnit.sample_id , curUnit);
                }
            }
        }
        
        
        Map<Integer, List<OutPutUnit>> patientIDMap = new HashMap();
        for (int sampleId : wholeMap.keySet()) {
            int patientId = wholeMap.get(sampleId).patient_id;
            patientIDMap.computeIfAbsent(patientId, x -> new ArrayList()).add(wholeMap.get(sampleId));
        }
        Map<Integer, List<OutPutUnit>> ctMap = new HashMap();
        for (int patientId : patientIDMap.keySet()) {
            int size = patientIDMap.get(patientId).size();
            ctMap.computeIfAbsent(size, x -> new ArrayList()).addAll(patientIDMap.get(patientId));
        }
        for(int i : ctMap.keySet()){
            System.out.println(ctMap.get(i).size());
        }
        return ctMap;
    }
    
    
    
    
    
    
    
    
    private Map<Integer, List<OutPutUnit>> getData(List<Panel> panelList, Map<Integer, double[]> trackingRangeMap) throws SQLException {
        Map<Integer, OutPutUnit> res = new HashMap();
        DataBaseCon db = new LXDataBaseCon();
        List<Integer> testLengthList = new ArrayList(); // length of each test_panel;
        for (Panel panel : panelList) {
            for (String partitionPanel : panel.getPanelList()) {
                String queryTableLen = "desc vibrant_america_test_result." + partitionPanel;
                ResultSet rs = db.read(queryTableLen);
                int testLength = 0;
                while (rs.next()) {
                    testLength++;
                }
                testLengthList.add(testLength);
            }
        }
        //build query
        StringBuilder queryStart = new StringBuilder("SELECT pd.patient_id ,sd.sample_id, date_format(now() , '%Y') - date_format(patient_birthdate , '%Y') as Age , patient_gender, patient_height, patient_weight , sample_collection_time,sd.sample_primary_diagnosis_code,sd.sample_secondary_diagnosis_code,sd.sample_third_diagnosis_code,sd.sample_fourth_diagnosis_code,sd.sample_additional_diagnosis_codes,");
        StringBuilder queryMiddle = new StringBuilder(" from ");

        boolean first = true;
        String joinBase = "";
        for (Panel panel : panelList) {
            for (String partitionPanel : panel.getPanelList()) {
                String resultPanel = "vibrant_america_test_result." + partitionPanel;
                String MasterPanel = "vibrant_america_test_result_ml.master_list" + partitionPanel.substring(partitionPanel.indexOf("_"));
                queryStart.append(resultPanel + ".*,");
                queryStart.append(MasterPanel + ".*,");

                if (first) {
                    joinBase = resultPanel + ".sample_id";
                    first = false;
                    queryMiddle.append(resultPanel).append(" join " + MasterPanel + " on " + MasterPanel + ".sample_id = " + joinBase);
                    continue;
                }
                queryMiddle.append(" join " + resultPanel + " on " + resultPanel + ".sample_id = " + joinBase);
                queryMiddle.append(" join " + MasterPanel + " on " + MasterPanel + ".sample_id = " + joinBase);
            }
        }

        queryMiddle.append(" join vibrant_america_information.sample_data sd on sd.sample_id = " + joinBase);
        queryMiddle.append(" join vibrant_america_information.`patient_details` pd on sd.patient_id = pd.patient_id where sd.customer_id < 900000 ;");

        queryStart.append("sd.julien_barcode");
//        queryStart.setLength(queryStart.length() - 1);
        String query = queryStart.toString() + queryMiddle.toString();

        System.out.println(query);
        ResultSet rsData = db.read(query);
        int colCt = rsData.getMetaData().getColumnCount();

        int sum = testLengthList.get(0);
        int index = 0;
        for (int k = 1; k <= colCt; k++) {
            if (rsData.getMetaData().getColumnName(k).equals("sample_id") && k > initialColumn) {
                continue;
            }
            if (k > initialColumn + sum) {
                k += testLengthList.get(index);
                sum += testLengthList.get(index++);
                if (index == testLengthList.size()) {
                    break;
                }
                sum += testLengthList.get(index);

            }
            titleList.add(rsData.getMetaData().getColumnName(k));
            if (k > initialColumn + 1) {
                titleList.add(rsData.getMetaData().getColumnName(k) + "_Result");
            }

        }
//        titleList.add(rsData.getMetaData().getColumnName(colCt));
        System.out.println(titleList);
        StringBuilder julienBarcodeSB = new StringBuilder();
        if (testCode[0]) {
            while (rsData.next()) {
                int listIndex = 0;
                int ct = 0;
                List<Double> dataList = new ArrayList();

                for (int i = initialColumn + 2; i <= colCt; i++) {
//                if(rsData.getMetaData().getColumnLabel(i).equals("sample_id")){
//                    
//                    continue;
//                }
                    if (listIndex == testLengthList.size()) {
                        break;
                    }
                    if (i - initialColumn - ct <= testLengthList.get(listIndex)) {
                        String testName = rsData.getMetaData().getColumnName(i);

                        double unit = rsData.getDouble(i);
                        double refUnit = -2;
                        dataList.add(unit);

                        double[] ref = new double[]{-1, -1};
                        if (throidRefMap.containsKey(testName)) {
                            ref = throidRefMap.get(testName);
//                            System.out.println(testName);
                            if (ref[0] != 0 || ref[1] != 0) {
                                if (unit < ref[0]) {
                                    refUnit = -1;
                                } else if (unit <= ref[1]) {
                                    refUnit = 0;
                                } else {
                                    refUnit = 1;
                                }
                            }
                        } else {
                            int tracking_id = rsData.getInt(i + testLengthList.get(listIndex));
                            if (trackingRangeMap.containsKey(tracking_id)) {
                                ref = trackingRangeMap.get(tracking_id);
                                if (ref[0] != 0 || ref[1] != 0) {
                                    if (unit < ref[0]) {
                                        refUnit = -1;
                                    } else if (unit <= ref[1]) {
                                        refUnit = 0;
                                    } else {
                                        refUnit = 1;
                                    }
                                }
                            }
                        }
                        dataList.add(refUnit);
                    } else {
                        i += testLengthList.get(listIndex);
                        ct += 2 * testLengthList.get(listIndex);

                        listIndex++;
                    }
                }
                int sampleId = rsData.getInt(2);

//            private OutPutUnit(int patient_id, int sample_id , int Age , String gender , 
//                            String height ,String weight , String sampleCollectionTime ,
//                            List<Double> DataList ){
                List<String> ICDList = new ArrayList();
                for (int i = 8; i <= initialColumn; i++) {
                    ICDList.add(rsData.getString(i));
                }

                res.put(sampleId, new OutPutUnit(rsData.getInt(1), sampleId, rsData.getInt(3), rsData.getString(4), rsData.getString(5), rsData.getString(6), rsData.getString(7), dataList, rsData.getInt(colCt), ICDList));

                julienBarcodeSB.append("'").append(rsData.getInt(colCt)).append("',");

            }
        } else {
            while (rsData.next()) {
                int listIndex = 0;
                int ct = 0;
                List<Double> dataList = new ArrayList();

                for (int i = initialColumn + 2; i <= colCt; i++) {
//                if(rsData.getMetaData().getColumnLabel(i).equals("sample_id")){
//                    
//                    continue;
//                }
                    if (listIndex == testLengthList.size()) {
                        break;
                    }
                    if (i - initialColumn - ct <= testLengthList.get(listIndex)) {
                        String tmpUnit = rsData.getString(i);
                        double unit;
                        if (tmpUnit.charAt(0) == '<') {
                            unit = Double.parseDouble(tmpUnit.substring(1)) - 0.01;
                        } else if (tmpUnit.charAt(0) == '>') {
                            unit = Double.parseDouble(tmpUnit.substring(1)) + 0.01;
                        } else if (!Character.isDigit(tmpUnit.charAt(0)) && tmpUnit.charAt(0) != '-') {
                            unit = -1.0;
                        } else {
                            unit = rsData.getDouble(i);
                        }

                        dataList.add(unit);
                        int tracking_id = rsData.getInt(i + testLengthList.get(listIndex));
                        double[] ref = new double[]{-1, -1};
                        double refUnit = -2;
                        if (trackingRangeMap.containsKey(tracking_id)) {
                            ref = trackingRangeMap.get(tracking_id);
                            if (ref[0] != 0 || ref[1] != 0) {
                                if (unit < ref[0]) {
                                    refUnit = -1;
                                } else if (unit <= ref[1]) {
                                    refUnit = 0;
                                } else {
                                    refUnit = 1;
                                }
                            }
                        }
                        dataList.add(refUnit);
                    } else {
                        i += testLengthList.get(listIndex);
                        ct += 2 * testLengthList.get(listIndex);

                        listIndex++;
                    }
                }
                int sampleId = rsData.getInt(2);

                List<String> ICDList = new ArrayList();
                for (int i = 8; i <= initialColumn; i++) {
                    ICDList.add(rsData.getString(i));
                }

//            private OutPutUnit(int patient_id, int sample_id , int Age , String gender , 
//                            String height ,String weight , String sampleCollectionTime ,
//                            List<Double> DataList ){
                res.put(sampleId, new OutPutUnit(rsData.getInt(1), sampleId, rsData.getInt(3), rsData.getString(4), rsData.getString(5), rsData.getString(6), rsData.getString(7), dataList, rsData.getInt(colCt), ICDList));
                julienBarcodeSB.append("'").append(rsData.getInt(colCt)).append("',");
            }
        }

//
//        for(int sampleId : res.keySet()){
//            System.out.println(sampleId);
//            System.out.println(res.get(sampleId).DataList);
//        }
        //sorted by patient_id
        Map<Integer, List<OutPutUnit>> patientIDMap = new HashMap();
        for (int sampleId : res.keySet()) {
            int patientId = res.get(sampleId).patient_id;
            patientIDMap.computeIfAbsent(patientId, x -> new ArrayList()).add(res.get(sampleId));

//            if (!patientIDMap.containsKey(patientId)) {
//                patientIDMap.put(patientId, new ArrayList(Arrays.asList(res.get(sampleId))));
//            } else {
//                patientIDMap.get(patientId).add(res.get(sampleId));
//            }
        }
        Map<Integer, List<OutPutUnit>> ctMap = new HashMap();
        for (int patientId : patientIDMap.keySet()) {
            int size = patientIDMap.get(patientId).size();
            ctMap.computeIfAbsent(size, x -> new ArrayList()).addAll(patientIDMap.get(patientId));
//            if (ctMap.containsKey(size)) {
//                ctMap.get(size).addAll(patientIDMap.get(patientId));
//            } else {
//                ctMap.put(size, new ArrayList(patientIDMap.get(patientId)));
//            }
        }

//        for(int size : ctMap.keySet()){
//            System.out.println(size);
////            System.out.println(ctMap.get(size));
//        }
        if (!testCode[1]) {
            julienBarcodeSB.setLength(julienBarcodeSB.length() - 1);

            String SymSql = "select a.julien_barcode , title , answer from \n"
                    + "(SELECT  concat(sq.question_value , psd.answer_id) title  ,  if(psd.user_typed is null , sa.answer , psd.user_typed ) answer , psl.survey_date da, julien_barcode \n"
                    + "\n"
                    + "FROM patient_profile.patient_survey_link psl\n"
                    + "\n"
                    + "JOIN patient_profile.patient_survey_data psd ON psl.save_id = psd.save_id\n"
                    + "\n"
                    + "JOIN patient_profile.survey_answers sa ON psd.answer_id = sa.answer_id\n"
                    + "\n"
                    + "JOIN patient_profile.survey_questions sq ON psd.question_id = sq.question_id\n"
                    + "\n"
                    + "WHERE psl.julien_barcode in (" + julienBarcodeSB.toString() + ") order by julien_barcode , psl.survey_date  desc) as a\n"
                    + "join\n"
                    + "(SELECT julien_barcode , max(survey_date)\n"
                    + "\n"
                    + "FROM patient_profile.patient_survey_link psl\n"
                    + "\n"
                    + "JOIN patient_profile.patient_survey_data psd ON psl.save_id = psd.save_id\n"
                    + "\n"
                    + "JOIN patient_profile.survey_answers sa ON psd.answer_id = sa.answer_id\n"
                    + "\n"
                    + "JOIN patient_profile.survey_questions sq ON psd.question_id = sq.question_id\n"
                    + "\n"
                    + "WHERE psl.julien_barcode in (" + julienBarcodeSB.toString() + ") group by julien_barcode) as b\n"
                    + "on a.julien_barcode = b.julien_barcode;";

//        System.out.println(SymSql);
            ResultSet rsSymRs = db.read(SymSql);
            while (rsSymRs.next()) {
                SymMap.computeIfAbsent(rsSymRs.getInt(1), x -> new ArrayList()).add(new String[]{rsSymRs.getString(2), rsSymRs.getString(3)});
            }
            updateTitle();
        }

        db.close();
        return ctMap;
    }

    private void updateTitle() {
        LinkedHashSet<String> set = new LinkedHashSet();
        titleColMap = new HashMap();
        for (List<String[]> list : SymMap.values()) {
            for (String[] qa : list) {
                set.add(qa[0]);
            }
        }
        int ct = titleList.size();
        for (String ti : set) {
            titleList.add(ti);
            titleColMap.put(ti, ct++);
        }

    }

    private Map<Integer, double[]> getRefMap(List<Panel> panelList) throws SQLException {
        Map<Integer, double[]> res = new HashMap();

        StringBuilder frontQuery = new StringBuilder();
        StringBuilder middleQuery = new StringBuilder();
        StringBuilder endQuery = new StringBuilder();
        frontQuery.append("select ");

        String joinBase = "";
        boolean first = true;
        for (Panel panel : panelList) {
            for (String partitionPanel : panel.getPanelList()) {
                String resultPanel = "vibrant_america_test_result." + partitionPanel;
                String MasterPanel = "vibrant_america_test_result_ml.master_list" + partitionPanel.substring(partitionPanel.indexOf("_"));
                frontQuery.append(MasterPanel).append(".*,");

                if (first) {
                    first = false;
                    joinBase = "vibrant_america_test_result." + panelList.get(0).getPanelList().get(0) + ".sample_id";
                    middleQuery.append(" from ").append(resultPanel);
                    endQuery.append(" join ").append(MasterPanel).append(" on ").append(MasterPanel).append(".sample_id = ").append(joinBase);
                    continue;
                }

                middleQuery.append(" join ").append(resultPanel).append(" on ").append(resultPanel).append(".sample_id = ").append(joinBase);
                endQuery.append(" join ").append(MasterPanel).append(" on ").append(MasterPanel).append(".sample_id = ").append(joinBase);
            }
        }
        endQuery.append(";");
        frontQuery.setLength(frontQuery.length() - 1);
        String query = frontQuery + middleQuery.toString() + endQuery.toString();
        System.out.println(query);

        DataBaseCon db = new LXDataBaseCon();
        ResultSet rs = db.read(query);
        HashSet<Integer> trackingIdSet = new HashSet();
        int rowCt = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            for (int i = 0; i < rowCt; i++) {

                if (rs.getMetaData().getColumnLabel(i + 1).equals("sample_id")) {
                    continue;
                }
//                System.out.print(" " + rs.getInt(i + 1));
                trackingIdSet.add(rs.getInt(i + 1));
            }
//            System.out.println();
        }

        //get refer range
        StringBuilder sb = new StringBuilder();
        for (int trackingId : trackingIdSet) {
            sb.append(trackingId + ",");
        }
        sb.setLength(sb.length() - 1);

        String refRangeQuery = "SELECT tracking_id , normal_min, normal_max FROM vibrant_america_information.report_master_list_tracking where tracking_id in (" + sb.toString() + ");";
        System.out.println(refRangeQuery);
        ResultSet rsRangeRs = db.read(refRangeQuery);
        while (rsRangeRs.next()) {
            res.put(rsRangeRs.getInt(1), new double[]{rsRangeRs.getDouble(2), rsRangeRs.getDouble(3)});
        }
        db.close();
        return res;
    }
    private Map<Integer, List<String[]>> deepCopy(Map<Integer, List<String[]>> map) {
        Map<Integer, List<String[]>> res = new HashMap();
        for(int i : map.keySet()){
            res.put(i , new ArrayList());
            for(String[] arr : map.get(i)){
                res.get(i).add(new String[]{arr[0] , arr[1]});
            }
        }
      
        return res;
    }

}
