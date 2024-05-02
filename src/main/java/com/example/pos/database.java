package com.example.pos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class database {
    public static Connection connectionDb(){

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/posData", "root","");
            return connect;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
}
