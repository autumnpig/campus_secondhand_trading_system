package com.secondhand.servlet;

import com.secondhand.dao.ProductDao;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 我的商品管理 Servlet
 * 映射路径: /myproducts
 */
@WebServlet("/myproducts")
public class MyProductsServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        // 1. 权限拦截
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. 获取操作参数并执行操作（删除/标记已售）
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        int productId = -1;

        // ... (ID解析和安全检查逻辑，确保商品属于当前用户) ...
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                productId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "非法的商品ID参数。");
            }
        }

        if (productId != -1 && action != null) {
            Product product = productDao.getProductById(productId);
            if (product == null || product.getUserId() != currentUser.getUserId()) {
                request.setAttribute("error", "操作失败：商品不存在或不属于当前用户。");
            } else {
                boolean success = false;
                String message;
                switch (action) {
                    case "delete":
                        success = productDao.deleteProduct(productId);
                        message = success ? "商品删除成功！" : "商品删除失败，请重试。";
                        request.setAttribute(success ? "success" : "error", message);
                        break;
                    case "mark_sold":
                        success = productDao.updateProductStatus(productId, 1); // 1: 已售
                        message = success ? "商品已成功标记为 [已售出]！" : "状态更新失败，请重试。";
                        request.setAttribute(success ? "success" : "error", message);
                        break;
                }
            }
        }

        // 3. 获取并展示商品列表
        List<Product> productList = productDao.getProductsByUserId(currentUser.getUserId());
        request.setAttribute("productList", productList);

        request.getRequestDispatcher("/my_products.jsp").forward(request, response);
    }

    // POST 和 GET 请求都走 doGet 逻辑
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}