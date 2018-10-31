/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.SQLException;

/**
 *
 * @author Wei Wang
 */
public class V7DataBaseCon extends DataBaseCon {   
    private static String name_V7 = "jdbc:mysql://192.168.10.121/tsp_test_unit_data?useSSL=false",user_V7 = "TSPI3", pw_V7 = "000028";
    public V7DataBaseCon() throws SQLException{
        super(name_V7 ,user_V7 , pw_V7 );
    }
}
