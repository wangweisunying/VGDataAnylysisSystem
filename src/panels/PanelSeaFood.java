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
public class PanelSeaFood extends Panel {
    public PanelSeaFood(){
        this.panelList = new ArrayList();
        panelList.addAll(Arrays.asList("result_seafood_zoomer_panel1" , 
                                        "result_seafood_zoomer_panel2",
                                        "result_seafood_zoomer_panel3",
                                        "result_seafood_zoomer_panel4",
                                        "result_seafood_zoomer_panel5",
                                        "result_seafood_zoomer_panel6",
                                        "result_seafood_zoomer_panel7",
                                        "result_seafood_zoomer_panel8"));
    }
}
