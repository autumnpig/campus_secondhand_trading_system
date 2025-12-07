package com.secondhand.dao;

import com.secondhand.entity.Category;
import com.secondhand.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类数据访问对象 (DAO)
 * 负责 t_category 表的数据库操作，主要用于获取分类列表。
 */
public class CategoryDao {

    /**
     * 获取所有商品分类列表
     * @return 分类列表
     */
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 假设分类ID越小，越靠前
        String sql = "SELECT category_id, category_name, description FROM t_category ORDER BY category_id ASC";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return categoryList;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                categoryList.add(category);
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch category list: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return categoryList;
    }
}