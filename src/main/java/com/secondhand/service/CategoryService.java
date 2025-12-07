package com.secondhand.service;

import com.secondhand.dao.CategoryDao;
import com.secondhand.entity.Category;

import java.util.List;

/**
 * 商品分类业务逻辑层 (Service)
 * 负责获取分类数据等业务操作。
 */
public class CategoryService {

    private final CategoryDao categoryDao = new CategoryDao();

    /**
     * 获取所有商品分类列表
     * @return 分类列表
     */
    public List<Category> getAllCategories() {
        // 直接调用 DAO 层获取数据
        return categoryDao.getAllCategories();
    }
}