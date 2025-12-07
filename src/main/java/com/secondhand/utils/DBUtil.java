package com.secondhand.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接工具类，用于获取和关闭MySQL连接。
 * 对应报告 4.2.1 DBUtil.java
 */
public class DBUtil {

    // 数据库连接信息 - 【请修改为你自己的配置！】
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    // 数据库URL，连接到 campus_secondhand 数据库
    private static final String URL = "jdbc:mysql://localhost:3306/campus_trading?serverTimezone=UTC&useSSL=false";
    // MySQL 用户名
    private static final String USER = "root";
    // MySQL 密码
    private static final String PASSWORD = "123456";

    // 静态代码块：加载驱动
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("Driver Loaded: " + DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver loading failed! Check pom.xml dependency.");
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     * @return Connection object
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed! Check URL, user, and password.");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭资源 (连接、语句、结果集)
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重载方法，只关闭连接 (用于不涉及查询的简单操作)
     */
    public static void close(Connection conn) {
        close(conn, null, null);
    }
}