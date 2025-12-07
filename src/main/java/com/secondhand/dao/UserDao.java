package com.secondhand.dao;

import com.secondhand.entity.User;
import com.secondhand.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 用户数据访问对象 (DAO)，负责t_user表的数据库操作。
 */
public class UserDao {

    public User login(String username, String password) {
        // ... (login 方法保持不变)
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        String sql = "SELECT user_id, username, nickname, phone, address, role, create_time " +
                "FROM t_user WHERE username = ? AND password = ?";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                System.err.println("Database connection failed, cannot execute login query.");
                return null;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setNickname(rs.getString("nickname"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getInt("role"));

                Timestamp createTime = rs.getTimestamp("create_time");
                if (createTime != null) {
                    user.setCreateTime(createTime.toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            System.err.println("Login query failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return user;
    }

    /**
     * 检查用户名（学号）是否已存在
     * 【修正逻辑：避免连接失败时误报存在】
     * @param username 待检查的用户名
     * @return 如果存在返回 true，否则返回 false
     */
    public boolean checkUsernameExist(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exist = false;

        String sql = "SELECT 1 FROM t_user WHERE username = ?";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                // 连接失败，不应该假定用户存在。应该记录错误，并返回 false 或抛出异常。
                // 为了让 Servlet 继续，我们先返回 false，让它去尝试注册，但 register 方法会失败。
                System.err.println("Warning: DB Connection failed in checkUsernameExist. Assuming not exist.");
                return false;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            exist = rs.next();
            if (exist) {
                System.out.println(">>> [Debug] Username check: " + username + " EXISTS.");
            } else {
                System.out.println(">>> [Debug] Username check: " + username + " NOT EXISTS.");
            }


        } catch (SQLException e) {
            System.err.println("Check username existence failed with SQL error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return exist;
    }

    /**
     * 插入新的用户记录（注册）
     * @param user 包含注册信息的 User 实体
     * @return 插入成功返回 true，否则返回 false
     */
    public boolean register(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        String sql = "INSERT INTO t_user (username, password, nickname, phone, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                System.err.println("Warning: DB Connection failed in register. Returning false.");
                return false; // 连接失败，返回失败
            }

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setInt(6, user.getRole());

            int rowsAffected = pstmt.executeUpdate();

            success = rowsAffected > 0;

        } catch (SQLException e) {
            // 这才是核心问题所在！
            System.err.println("FATAL: User registration failed with SQL error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        return success;
    }
}