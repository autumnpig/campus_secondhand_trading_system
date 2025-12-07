package com.secondhand.entity;

/**
 * 商品分类实体类 (JavaBean)
 * 对应数据库表 t_category
 */
public class Category {
    private int categoryId;
    private String categoryName;
    private String description;

    // 无参构造函数
    public Category() {
    }

    // --- Getter and Setter ---

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}