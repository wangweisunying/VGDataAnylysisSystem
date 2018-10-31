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
public class PanelLectin extends Panel {
    public PanelLectin(){
        this.panelList = new ArrayList();
        panelList.addAll(Arrays.asList("result_lectin_aquaporin_panel1" , "result_lectin_aquaporin_panel2"));
    }
}
