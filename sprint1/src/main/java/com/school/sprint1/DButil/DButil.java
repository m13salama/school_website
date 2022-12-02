package com.school.sprint1.DButil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DButil {
    private static Connection connection = null;

    public static Connection getConnection(){
        if(connection != null) return connection;

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/SCHOOL?useSSL=false";
        String user = "root";
        String password = "@ahmed1102001";

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,user,password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
