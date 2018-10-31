package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wei Wang
 */
public class DataBaseCon {
    protected Statement stmt;
    protected DataBaseCon(String name , String user , String pw) throws SQLException{
        Connection mycon = DriverManager.getConnection(name, user, pw);
        stmt = mycon.createStatement();
    }
    public ResultSet read(String sql) throws SQLException{
        return stmt.executeQuery(sql);
    }
    
    public void write(String sql) throws SQLException{
        stmt.executeUpdate(sql);
    }
    public void close() throws SQLException{
        stmt.getConnection().close();
    }
    
}

//class V7DataBaseCon extends DataBaseCon {   
//    private static String name_V7 = "jdbc:mysql://192.168.10.121/tsp_test_unit_data?useSSL=false",user_V7 = "TSPI3", pw_V7 = "000028";
//    public V7DataBaseCon() throws SQLException{
//        super(name_V7 ,user_V7 , pw_V7 );
//    }
//}

//class LXDataBaseCon extends DataBaseCon {   
//    private static String name_LX = "jdbc:mysql://192.168.10.153/vibrant_america_information?useSSL=false",user_LX = "wang", pw_LX = "wang";
//    public LXDataBaseCon() throws SQLException{
//        super(name_LX ,user_LX , pw_LX);
//    }
//}

class VGDataBaseCon extends DataBaseCon {   
    private static String name_VG = "jdbc:mysql://192.168.10.187/lis_lab_info?useSSL=false",user_VG = "VG", pw_VG = "vibrant@2015";
    public VGDataBaseCon() throws SQLException{
        super(name_VG ,user_VG , pw_VG);
    }
}


class LocalHost extends DataBaseCon {   
    private static String name_lo = "jdbc:mysql://localhost/test?useSSL=false",user_lo = "root", pw_lo = "000028";
    public LocalHost() throws SQLException{
        super(name_lo ,user_lo , pw_lo);
    }
}



