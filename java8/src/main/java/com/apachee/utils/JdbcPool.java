package com.apachee.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class JdbcPool {

    private static LinkedList<Connection> connectionQueue = new LinkedList<>();

    public synchronized static Connection getConnection() {
        try {
            if (connectionQueue.isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spark?useSSL=false&serverTimezone=UTC", "root", "123456");
                    connectionQueue.push(conn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connectionQueue.pop();
    }

    public static void returnConnection(Connection conn) {
        connectionQueue.push(conn);
    }
}
