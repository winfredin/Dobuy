package com;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/dobuy?serverTimezone=Asia/Taipei";
        String username = "root";
        String password = "123456789";

        try {
            // 加載 MySQL 驅動（可選，現代版本可自動加載）
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立連線
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("資料庫連線成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
