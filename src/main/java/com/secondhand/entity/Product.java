package com.secondhand.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息实体类 (JavaBean)
 * 对应数据库表 t_product
 */
public class Product {
    private int productId;
    private String productName;
    private String description;
    private BigDecimal price; // 使用 BigDecimal 存储价格，避免浮点数精度问题
    private String imageUrl; // 存储图片文件路径
    private int status; // 状态 (0:在售, 1:已售, 2:下架)
    private int categoryId; // 所属分类ID (外键)
    private int userId; // 发布者ID (外键)
    private LocalDateTime publishTime;

    // 无参构造函数
    public Product() {
    }

    // --- Getter and Setter ---

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }
}