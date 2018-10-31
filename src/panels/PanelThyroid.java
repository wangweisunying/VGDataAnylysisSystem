/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Wei Wang
 */
public class PanelThyroid extends Panel {
    private Map<String , double[]> refMap;
    public PanelThyroid(){
        this.panelList = new ArrayList();
        panelList.addAll(Arrays.asList("result_thyroid_panel"));
    
        this.refMap = new HashMap();
        refMap.put("T4", new double[]{4.5 , 11.7});
        refMap.put("FT4", new double[]{0.9 , 1.7});
        refMap.put("FT3", new double[]{2.8 , 4.4});
        refMap.put("T3", new double[]{0.8 , 2});
        refMap.put("TSH", new double[]{0.3 , 4.2});
        refMap.put("ATPO", new double[]{0 , 9});
        refMap.put("RT3", new double[]{10 , 24});
        refMap.put("A-TG", new double[]{0 , 4});
    }
    public Map<String , double[]> getRefMap(){
        return this.refMap;
    }
}
