package com.apachee.datatransfer;

import oracle.sql.STRUCT;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.*;

public class OracleToPostgre {


    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        String url = "jdbc:oracle:thin:@127.0.0.1:15210:Fast";
        String user = "c##fast484";
        String password = "Fast*123";
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from cfg_district_border");
        OutputStream os = new FileOutputStream("data/output/cfg_map_region.csv");
        while(resultSet.next()) {
            String line = resultSet.getString("city")+","+
                    resultSet.getString("planet_id")+","+
                    resultSet.getString("border_name").replaceAll("\n","").replaceAll("\n","")+","+
                    resultSet.getString("centroidx")+","+
                    resultSet.getString("centroidy")+","+
                    resultSet.getString("vmap")+"\r\n";
            System.out.println(line);
            os.write(line.getBytes());
        }
        os.flush();
        os.close();

    }
}
