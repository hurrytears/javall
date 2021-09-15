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
        ResultSet resultSet = statement.executeQuery("select * from CFG_MAP_SCENE_LOAD11");
        OutputStream os = new FileOutputStream("data/output/cfg_map_scene.csv");
        while(resultSet.next()) {
            String line = resultSet.getString("city")+","+
                    resultSet.getString("planet_id")+","+
                    resultSet.getString("scene_name").replaceAll("\n","").replaceAll("\n","")+","+
                    resultSet.getString("centroidx")+","+
                    resultSet.getString("centroidy")+",";
            String[] vmap = resultSet.getString("vmap").split(";");
            if(vmap[0].equals(vmap[vmap.length-1])){
                line = line + resultSet.getString("vmap")+"\r\n";
            }else {
                line = line + resultSet.getString("vmap")+ ";" + vmap[0] +"\r\n";
            }
            os.write(line.getBytes());
        }
        os.flush();
        os.close();

    }
}
