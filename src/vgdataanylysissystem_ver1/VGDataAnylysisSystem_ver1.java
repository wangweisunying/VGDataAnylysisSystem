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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
import panels.PanelEgg;
import panels.PanelHormonal;
import panels.PanelLectin;
import panels.PanelLiver;
import panels.PanelMicronutrients;
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
        
      private static TestPanel[] testList = {TestPanel.THYROID ,TestPanel.MICRONUTRIENTS };
      private String path = "C:\\Users\\Wei Wang\\Desktop\\VGANAlysis\\testOutPut\\sample.xlsx";

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
      
    private enum TestPanel{
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
        CREATININE
    }  
      
      
    private class OutPutUnit {

        private List<Double> DataList;
        private int patient_id, sample_id, Age;
        private String gender, height, weight, sampleCollectionTime, symptoms;

        private OutPutUnit(int patient_id, int sample_id, int Age, String gender,
                String height, String weight, String sampleCollectionTime,
                List<Double> DataList ,String symptoms) {
            this.patient_id = patient_id;
            this.sample_id = sample_id;
            this.Age = Age;
            this.gender = gender;
            this.height = height;
            this.weight = weight;
            this.sampleCollectionTime = sampleCollectionTime;
            this.DataList = DataList;
            this.symptoms = symptoms;
        }
    }

    
    private List<String> titleList = new ArrayList();
    private Map<String, double[]> throidRefMap;

    public static void main(String[] args) throws SQLException, IOException, MessagingException {
        VGDataAnylysisSystem_ver1 test = new VGDataAnylysisSystem_ver1();
        List<Panel> panelList = test.convertToList(testList);
        boolean hasThroid = test.preCheck(panelList);
        Map<Integer, List<OutPutUnit>> dataMap = test.getData(panelList, test.getRefMap(panelList), hasThroid);
        test.exportToExcel(dataMap);
//        test.sendEmail();
        
        
    }
    
    private void sendEmail() throws MessagingException{
        StringBuilder sb = new StringBuilder();
        for(TestPanel x : testList){
            sb.append(x.toString() + "vs");
        }
        sb.setLength(sb.length() - 2);

        EmailAndText.sendEmail("", "", "thushanis@vibrantgenomics.com", "VG Test Report Auto Mail--- Please do not reply", sb.toString(), path);
    }
    
    private List<Panel> convertToList(TestPanel[] testList) {
        List<Panel> res = new ArrayList();
        for(TestPanel test :  testList){
            switch(test){
                case CORN:
                    res.add(new PanelCorn());
                    break;
                case DAIRY:
                    res.add(new PanelDairy());
                    break;
                case EGG:
                    res.add(new PanelEgg());
                    break;
                case HORMONAL:
                    res.add(new PanelHormonal());
                    break;
                case LECTIN:
                    res.add(new PanelLectin());
                    break;
                case NUT:
                    res.add(new PanelNut());
                    break;
                case PEANUT:
                    res.add(new PanelPeanut());
                    break;
                case SEAFOOD:
                    res.add(new PanelSeaFood());
                    break;
                case SOY:
                    res.add(new PanelSoy());
                    break;
                case THYROID:
                    res.add(new PanelThyroid());
                    break;
                case LIVER:
                    res.add(new PanelLiver());
                    break;
                case MICRONUTRIENTS:
                    res.add(new PanelMicronutrients());
                    break;
                case CARDIOLOGY:
                    res.add(new PanelCardiology());
                    break;
                case WHEAT:
                    res.add(new PanelWheatZoomer());
                    break;
                case CREATININE:
                    res.add(new PanelCreatinine());
                    break;
//                   
//                    MICRONUTRIENTS,
//        CARDIOLOGY,
//        WHEAT
              
                 
            }
        }
        return res;
    }

    private boolean preCheck(List<Panel> panelList) {
        for (Panel panel : panelList) {
            if (panel instanceof PanelThyroid) {
                throidRefMap = ((PanelThyroid) panel).getRefMap();
                return true;
            }
        }
        return false;
    }

    private void exportToExcel(Map<Integer, List<OutPutUnit>> dataMap) throws IOException {
        Workbook wb = ExcelOperation.getWriteConnection(ExcelOperation.ExcelType.XLSX);
        
        if(dataMap.isEmpty()){
            System.out.println("There tests do not share patient!!!");
            return;
        }
        
        
        
        
        
        for (int visit : dataMap.keySet()) {

            Sheet sheet = wb.createSheet("visit_" + visit);
            int rowCt = 0;
            Row row = sheet.createRow(rowCt++);
            int colCt = 0;
            
            
            Map<String , Integer> symMap = new TreeMap();
            for (OutPutUnit unit : dataMap.get(visit)){
               if(unit.symptoms == null){
                   continue;
               }
               String[] arr = unit.symptoms.split("__");
               for(String x : arr){
                   symMap.putIfAbsent(x.split("&&")[0] , -1);
               }
            }
            
            
            
            for (String title : titleList) {
                row.createCell(colCt).setCellValue(title);
                sheet.autoSizeColumn(colCt++);
            }
            
            for (String sympTitle : symMap.keySet()){
                row.createCell(colCt).setCellValue(sympTitle);
                symMap.put(sympTitle, colCt);
                sheet.autoSizeColumn(colCt++);
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
                for (double x : unit.DataList) {
                    row.createCell(colCt++).setCellValue(x);
                }
                if(unit.symptoms != null){
                    String[] symptomsArr = unit.symptoms.split("__");
                    for(String sym : symptomsArr){
                        String[] chunk = sym.split("&&");
                        if(chunk.length < 2){
                            continue;
                        }
//             
                        row.createCell(symMap.get(chunk[0])).setCellValue(chunk[1]);
                    }
                }
                
            }

        }
        
        ExcelOperation.writeExcel(path, wb);
        wb.close();
    }

    private Map<Integer, List<OutPutUnit>> getData(List<Panel> panelList, Map<Integer, double[]> trackingRangeMap, boolean hasThriod) throws SQLException {
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
        StringBuilder queryStart = new StringBuilder("SELECT pd.patient_id ,sd.sample_id, date_format(now() , '%Y') - date_format(patient_birthdate , '%Y') as Age , patient_gender, patient_height, patient_weight , sample_collection_time,");
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
        queryMiddle.append(" join vibrant_america_information.`patient_details` pd on sd.patient_id = pd.patient_id left join patient_profile.patient_survey_link psl on psl.julien_barcode = sd.julien_barcode\n" +
"		left JOIN patient_profile.patient_survey_data psd ON psl.save_id = psd.save_id\n" +
"		left JOIN patient_profile.survey_answers sa ON psd.answer_id = sa.answer_id\n" +
"		left JOIN patient_profile.survey_questions  sq  ON psd.question_id = sq.question_id where sd.customer_id < 900000  group by sd.sample_id ;");
        
        
        queryStart.append("group_concat( concat(sq.question_value , '&&', if(psd.user_typed is null  ,sa.answer , psd.user_typed)) separator '__') AS symptoms");
//        queryStart.setLength(queryStart.length() - 1);
        String query = queryStart.toString() + queryMiddle.toString();

        System.out.println(query);
        ResultSet rsData = db.read(query);
        int colCt = rsData.getMetaData().getColumnCount();

        int sum = testLengthList.get(0);
        int index = 0;
        for (int k = 1; k <= colCt; k++) {
            if (rsData.getMetaData().getColumnName(k).equals("sample_id") && k > 7) {
                continue;
            }
            if (k > 7 + sum) {
                k += testLengthList.get(index);
                sum += testLengthList.get(index++);
                if (index == testLengthList.size()) {
                    break;
                }
                sum += testLengthList.get(index);

            }
            titleList.add(rsData.getMetaData().getColumnName(k));
            if (k > 8) {
                titleList.add(rsData.getMetaData().getColumnName(k) + "_Result");
            }

        }
//        titleList.add(rsData.getMetaData().getColumnName(colCt));
        System.out.println(titleList);

        if (hasThriod) {
            while (rsData.next()) {
                int listIndex = 0;
                int ct = 0;
                List<Double> dataList = new ArrayList();

                for (int i = 9; i <= colCt; i++) {
//                if(rsData.getMetaData().getColumnLabel(i).equals("sample_id")){
//                    
//                    continue;
//                }
                    if (listIndex == testLengthList.size()) {
                        break;
                    }
                    if (i - 7 - ct <= testLengthList.get(listIndex)) {
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
                res.put(sampleId, new OutPutUnit(rsData.getInt(1), sampleId, rsData.getInt(3), rsData.getString(4), rsData.getString(5), rsData.getString(6), rsData.getString(7), dataList , rsData.getString(colCt)));
            }
        } else {
            while (rsData.next()) {
                int listIndex = 0;
                int ct = 0;
                List<Double> dataList = new ArrayList();

                for (int i = 9; i <= colCt; i++) {
//                if(rsData.getMetaData().getColumnLabel(i).equals("sample_id")){
//                    
//                    continue;
//                }
                    if (listIndex == testLengthList.size()) {
                        break;
                    }
                    if (i - 7 - ct <= testLengthList.get(listIndex)) {
                        String tmpUnit = rsData.getString(i);
                        double unit;
                        if(tmpUnit.charAt(0) == '<'){
                            unit = Double.parseDouble(tmpUnit.substring(1)) - 0.01;
                        }
                        else if(tmpUnit.charAt(0) == '>'){
                            unit = Double.parseDouble(tmpUnit.substring(1)) + 0.01;
                        }
                        else if(!Character.isDigit(tmpUnit.charAt(0))){
                            unit = -1.0;
                        }
                        else{
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

//            private OutPutUnit(int patient_id, int sample_id , int Age , String gender , 
//                            String height ,String weight , String sampleCollectionTime ,
//                            List<Double> DataList ){
                res.put(sampleId, new OutPutUnit(rsData.getInt(1), sampleId, rsData.getInt(3), rsData.getString(4), rsData.getString(5), rsData.getString(6), rsData.getString(7), dataList , rsData.getString(colCt)));
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
            ctMap.computeIfAbsent(size , x-> new ArrayList()).addAll(patientIDMap.get(patientId));
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
        db.close();
        return ctMap;
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

}
