<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.secondhand.entity.*" %>
<%@ page import="com.secondhand.service.CategoryService" %>
<%@ page import="java.util.List" %>
<%
  // 获取 Servlet 传过来的商品对象
  Product p = (Product) request.getAttribute("p");
  if (p == null) { response.sendRedirect("myproducts"); return; }

  // 获取分类列表（为了下拉框）
  CategoryService cs = new CategoryService();
  List<Category> categoryList = cs.getAllCategories();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>编辑商品</title>
  <link rel="stylesheet" href="css/main.css">
</head>
<body>
<div class="publish-container">
  <h2>✏️ 编辑商品信息</h2>

  <form action="edit_product" method="post" enctype="multipart/form-data">
    <input type="hidden" name="product_id" value="<%= p.getProductId() %>">
    <input type="hidden" name="old_image" value="<%= p.getImageUrl() %>">

    <div class="form-group">
      <label>商品名称</label>
      <input type="text" name="product_name" class="form-control" value="<%= p.getProductName() %>" required>
    </div>

    <div class="form-group">
      <label>商品分类</label>
      <select name="category_id" class="form-control" required>
        <% for(Category c : categoryList) { %>
        <option value="<%= c.getCategoryId() %>" <%= c.getCategoryId() == p.getCategoryId() ? "selected" : "" %>>
          <%= c.getCategoryName() %>
        </option>
        <% } %>
      </select>
    </div>

    <div class="form-group">
      <label>价格 (元)</label>
      <input type="number" name="price" class="form-control" step="0.01" value="<%= p.getPrice() %>" required>
    </div>

    <div class="form-group">
      <label>详细描述</label>
      <textarea name="description" class="form-control" required><%= p.getDescription() %></textarea>
    </div>

    <div class="form-group">
      <label>商品图片 (不上传则保留原图)</label>
      <div style="margin-bottom: 10px;">
        <img src="<%= p.getImageUrl() %>" style="height: 100px; border-radius: 4px;">
        <span style="color: #666; font-size: 12px;">当前图片</span>
      </div>
      <div class="file-input-wrapper">
        <input type="file" name="product_image" accept="image/*">
      </div>
    </div>

    <div class="form-group" style="text-align: center;">
      <button type="submit" class="btn btn-primary">保存修改</button>
      <a href="myproducts" style="display:block; margin-top:10px; color:#666;">取消</a>
    </div>
  </form>
</div>
</body>
</html>