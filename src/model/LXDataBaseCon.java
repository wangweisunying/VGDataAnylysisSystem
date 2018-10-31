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
public class LXDataBaseCon extends DataBaseCon {   
    private static String name_LX = "jdbc:mysql://192.168.10.153/vibrant_america_information?useSSL=false",user_LX = "wang", pw_LX = "wang";
    public LXDataBaseCon() throws SQLException{
        super(name_LX ,user_LX , pw_LX);
    }
}
