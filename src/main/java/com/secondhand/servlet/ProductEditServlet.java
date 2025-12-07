package com.secondhand.servlet;

import com.secondhand.dao.ProductDao;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@WebServlet("/edit_product")
public class ProductEditServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();
    // 需与 ProductPublishServlet 保持一致
    private static final String RELATIVE_UPLOAD_PATH = "images/product_images";
    // ⚠️ 请确保这里是你电脑上的实际路径，或者使用 getServletContext().getRealPath(...)
    private static final String ABSOLUTE_UPLOAD_ROOT = "D:/desk/web课设/CampusTradingSystem/src/main/webapp";

    // 处理 GET：显示编辑页面
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (idParam != null && currentUser != null) {
            Product product = productDao.getProductById(Integer.parseInt(idParam));
            // 权限检查：只能编辑自己的商品
            if (product != null && product.getUserId() == currentUser.getUserId()) {
                request.setAttribute("p", product);
                // 还需要分类列表供下拉框使用（偷懒做法：直接在这里调用Service或DAO获取，或者重用 publish 的逻辑）
                // 为了简单，这里建议复用 publish.jsp 的逻辑，或者在 JSP 里重新获取一下分类
                request.getRequestDispatcher("/edit_product.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect("myproducts"); // 失败回退
    }

    // 处理 POST：保存修改
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 权限与类型检查
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.sendRedirect("myproducts");
            return;
        }

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) { response.sendRedirect("login.jsp"); return; }

        Product productToUpdate = new Product();
        String newImagePath = null;
        String oldImagePath = null; // 用于若未上传新图时保持旧图

        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString("UTF-8");
                    switch (name) {
                        case "product_id": productToUpdate.setProductId(Integer.parseInt(value)); break;
                        case "product_name": productToUpdate.setProductName(value); break;
                        case "price": productToUpdate.setPrice(new BigDecimal(value)); break;
                        case "description": productToUpdate.setDescription(value); break;
                        case "category_id": productToUpdate.setCategoryId(Integer.parseInt(value)); break;
                        case "old_image": oldImagePath = value; break; // 获取隐藏域中的旧图路径
                    }
                } else {
                    // 处理文件上传
                    if (item.getName() != null && !item.getName().trim().isEmpty()) {
                        String fileName = UUID.randomUUID() + item.getName().substring(item.getName().lastIndexOf("."));
                        String uploadPath = ABSOLUTE_UPLOAD_ROOT + File.separator + RELATIVE_UPLOAD_PATH.replace('/', File.separatorChar);
                        File storeFile = new File(uploadPath, fileName);
                        item.write(storeFile);
                        newImagePath = RELATIVE_UPLOAD_PATH + "/" + fileName;
                    }
                }
            }

            // 逻辑：如果有新图，用新图；否则用旧图
            productToUpdate.setImageUrl(newImagePath != null ? newImagePath : oldImagePath);
            productToUpdate.setUserId(currentUser.getUserId());

            boolean success = productDao.updateProduct(productToUpdate);

            if(success) {
                response.sendRedirect("myproducts?success=updated");
            } else {
                request.setAttribute("error", "更新失败");
                request.getRequestDispatcher("edit_product.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("myproducts?error=exception");
        }
    }
}