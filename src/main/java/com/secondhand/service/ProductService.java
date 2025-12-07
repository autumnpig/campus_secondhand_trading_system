package com.secondhand.service;

import com.secondhand.dao.ProductDao;
import com.secondhand.entity.Product;

import java.util.List;

/**
 * 商品业务逻辑层 (Service)
 * 负责调用 DAO 获取数据，并处理业务逻辑（目前仅为获取列表）。
 */
public class ProductService {

    private final ProductDao productDao = new ProductDao();

    /**
     * 获取首页展示的商品列表
     */
    public List<Product> getHomeProducts() {
        // 直接调用 DAO 层获取在售商品数据
        return productDao.getAllProducts();
    }
}