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

    /**
     * 根据商品ID查询商品详情
     * @param productId 商品的唯一ID
     * @return 匹配的商品对象或 null
     */
    public Product getProductById(int productId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Product product = null;

        // 查询指定 ID 的商品（不限制 status, 即使已售也应能查看详情）
        String sql = "SELECT * FROM t_product WHERE product_id = ?";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return null;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setImageUrl(rs.getString("image_url"));
                product.setStatus(rs.getInt("status"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setUserId(rs.getInt("user_id"));
                // (此处省略 publishTime 的封装，与 getAllProducts 类似)
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch product by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return product;
    }

    /**
     * 根据用户ID查询其发布的所有商品
     * @param userId 发布者ID
     * @return 商品列表
     */
    public List<Product> getProductsByUserId(int userId) {
        List<Product> productList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 查询指定 user_id 的所有商品，按发布时间倒序排列
        String sql = "SELECT * FROM t_product WHERE user_id = ? ORDER BY publish_time DESC";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return productList;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // ... (封装 Product 实体对象的逻辑，与 getAllProducts 类似，此处省略) ...
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
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
            System.err.println("Failed to fetch products by user ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return productList;
    }

    /**
     * 更新商品状态 (标记已售/重新上架)
     * @param productId 商品ID
     * @param status 新状态 (0:在售, 1:已售)
     * @return 更新成功返回 true，否则返回 false
     */
    public boolean updateProductStatus(int productId, int status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        String sql = "UPDATE t_product SET status = ? WHERE product_id = ?";
        // ... (数据库更新逻辑) ...
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, productId);
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update product status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        return success;
    }

    /**
     * 根据商品ID删除商品记录
     * @param productId 商品ID
     * @return 删除成功返回 true，否则返回 false
     */
    public boolean deleteProduct(int productId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        String sql = "DELETE FROM t_product WHERE product_id = ?";
        // ... (数据库删除逻辑) ...
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        return success;
    }

    public boolean updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        // 动态更新：如果图片路径不为空则更新图片，否则保留原图（SQL逻辑在Servlet处理更简单，这里假设传入的product已经包含了最终图片路径）
        String sql = "UPDATE t_product SET product_name=?, description=?, price=?, image_url=?, category_id=? WHERE product_id=? AND user_id=?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setString(4, product.getImageUrl()); // 这里的URL必须是最终确定的路径
            pstmt.setInt(5, product.getCategoryId());
            pstmt.setInt(6, product.getProductId());
            pstmt.setInt(7, product.getUserId()); // 安全校验：确保只能改自己的

            success = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
        return success;
    }

    /**
     * 根据关键词搜索在售商品 (模糊查询)
     * @param keyword 搜索关键词
     * @return 匹配的商品列表
     */
    public List<Product> searchProducts(String keyword) {
        List<Product> productList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // SQL: 在名称或描述中查找包含关键词的记录，且状态必须为 0 (在售)
        String sql = "SELECT * FROM t_product WHERE status = 0 AND (product_name LIKE ? OR description LIKE ?) ORDER BY publish_time DESC";

        try {
            conn = DBUtil.getConnection();
            if (conn == null) return productList;

            pstmt = conn.prepareStatement(sql);
            // 拼接通配符 %
            String param = "%" + keyword + "%";
            pstmt.setString(1, param);
            pstmt.setString(2, param);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
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
            System.err.println("Search products failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return productList;
    }
}