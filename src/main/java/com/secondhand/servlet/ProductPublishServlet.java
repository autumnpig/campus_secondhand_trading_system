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

/**
 * Product Publish Controller Servlet.
 * Mapped Path: /publish
 * Responsible for parsing multipart/form-data requests, saving files, and writing data to the database.
 */
@WebServlet("/publish")
public class ProductPublishServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();

    // 1. 定义上传文件在 Web 根目录下的相对路径 (Tomcat 访问路径)
    private static final String RELATIVE_UPLOAD_PATH = "images/product_images";

    private static final String ABSOLUTE_UPLOAD_ROOT = "D:/desk/web课设/CampusTradingSystem/src/main/webapp";

    // 2. Temporary file storage directory (remains the same)
    private static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");

    // Max file size allowed (5MB)
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 5;
    // Max request size allowed (10MB)
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/publish.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Permission check: ensure only logged-in users can publish
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Check if the request contains file upload data (multipart/form-data)
        if (!ServletFileUpload.isMultipartContent(request)) {
            request.setAttribute("error", "Form type error, please use multipart/form-data.");
            request.getRequestDispatcher("/publish.jsp").forward(request, response);
            return;
        }

        String uploadPath = ABSOLUTE_UPLOAD_ROOT + File.separator + RELATIVE_UPLOAD_PATH.replace('/', File.separatorChar);

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MAX_FILE_SIZE);
        factory.setRepository(new File(TEMP_DIRECTORY));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        upload.setHeaderEncoding("UTF-8");

        Product newProduct = new Product();
        String imagePath = null; // To store the final image URL/path

        try {
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) { // --- Process File Field (Image) ---
                        String fileName = new File(item.getName()).getName();

                        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                        File uploadDir = new File(uploadPath); // 使用新的物理路径
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        File storeFile = new File(uploadPath + File.separator + uniqueFileName);
                        item.write(storeFile); // Write file to disk

                        // 存储相对路径 (数据库路径)
                        imagePath = RELATIVE_UPLOAD_PATH + "/" + uniqueFileName;

                        System.out.println("Image saved to: " + storeFile.getAbsolutePath());

                    } else { // --- Process Regular Form Field (Text) ---
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString("UTF-8");

                        switch (fieldName) {
                            case "product_name":
                                newProduct.setProductName(fieldValue);
                                break;
                            case "category_id":
                                newProduct.setCategoryId(Integer.parseInt(fieldValue));
                                break;
                            case "price":
                                newProduct.setPrice(new BigDecimal(fieldValue));
                                break;
                            case "description":
                                newProduct.setDescription(fieldValue);
                                break;
                        }
                    }
                }
            }

            newProduct.setImageUrl(imagePath);
            newProduct.setUserId(currentUser.getUserId());

            boolean success = productDao.publishProduct(newProduct);

            if (success) {
                request.setAttribute("success", "商品发布成功！");
            } else {
                request.setAttribute("error", "商品信息写入数据库失败，请检查数据库配置。");
            }


        } catch (Exception ex) {
            request.setAttribute("error", "文件上传或解析失败：" + ex.getMessage());
            System.err.println("File upload or parsing error: " + ex.getMessage());
            ex.printStackTrace();
        }

        request.getRequestDispatcher("/publish.jsp").forward(request, response);
    }
}