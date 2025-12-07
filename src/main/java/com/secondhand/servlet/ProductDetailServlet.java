package com.secondhand.servlet;

import com.secondhand.dao.ProductDao;
import com.secondhand.dao.UserDao;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 商品详情页请求处理 Servlet
 * 映射路径: /detail
 * 负责根据商品ID查询商品信息和卖家联系方式。
 */
@WebServlet("/detail")
public class ProductDetailServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 1. 获取商品ID参数
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("index.jsp"); // 参数缺失，重定向回首页
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);

            // 2. 查询商品详情
            Product product = productDao.getProductById(productId);

            if (product != null) {
                // 3. 关联查询卖家信息
                User seller = userDao.getUserById(product.getUserId());

                // 4. 将数据存储在 Request 作用域中
                request.setAttribute("product", product);
                request.setAttribute("seller", seller);

                // 5. 转发到详情页
                request.getRequestDispatcher("/product_detail.jsp").forward(request, response);
            } else {
                // 商品不存在
                request.setAttribute("error", "商品ID不存在或已被删除。");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            // ID 不是有效的数字
            request.setAttribute("error", "非法的商品ID参数。");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}