/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panels;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Wei Wang
 */
public class PanelMicronutrients extends Panel {
    public PanelMicronutrients(){
        panelList = new ArrayList();
        panelList.addAll(Arrays.asList("result_micronutrients_v2_panel1" , "result_micronutrients_v2_panel2" , "result_micronutrients_v2_panel3"));
    }
}
