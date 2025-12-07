package com.secondhand.dao;

import com.secondhand.entity.Product;
import com.secondhand.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * 商品信息数据访问对象 (DAO)
 * 负责 t_product 表的数据库操作，用于发布新商品和查询商品。
 */
public class ProductDao {

    /**
     * 插入新的商品记录（发布商品）
     * [此方法保持不变，省略...]
     */
    public boolean publishProduct(Product product) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        // SQL 语句：插入商品的核心字段
        String sql = "INSERT INTO t_product (product_name, description, price, image_url, status, category_id, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setString(4, product.getImageUrl());
            pstmt.setInt(5, 0); // 默认状态: 0:在售
            pstmt.setInt(6, product.getCategoryId());
            pstmt.setInt(7, product.getUserId());
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Product publishing failed with SQL error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        return success;
    }

    /**
     * 查询所有在售商品
     * @return 商品列表
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 查询状态为 0 (在售) 的商品，按发布时间倒序排列
        String sql = "SELECT * FROM t_product WHERE status = 0 ORDER BY publish_time DESC";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return productList;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));

                // 注意：数据库中的 DECIMAL 字段需要用 getBigDecimal 获取
                product.setPrice(rs.getBigDecimal("price"));
                product.setImageUrl(rs.getString("image_url"));

                product.setStatus(rs.getInt("status"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setUserId(rs.getInt("user_id"));

                Timestamp ts = rs.getTimestamp("publish_time");
                if (ts != null) {
                    product.setPublishTime(ts.toLocalDateTime());
                }

                productList.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch product list: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return productList;
    }
}