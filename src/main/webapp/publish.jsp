<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.secondhand.entity.User" %>
<%@ page import="com.secondhand.entity.Category" %>
<%@ page import="com.secondhand.service.CategoryService" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  // 权限拦截
  User user = (User) session.getAttribute("currentUser");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }

// 获取分类数据
  CategoryService categoryService = new CategoryService();
  List<Category> categoryList = categoryService.getAllCategories();
  request.setAttribute("categories", categoryList);


%>

<!DOCTYPE html>

<html>
<head>
  <meta charset="UTF-8">
  <title>发布闲置商品</title>
  <link rel="stylesheet" href="css/main.css">
</head>
<body>

<!-- 使用 publish-container 保持统一卡片风格 -->
<div class="publish-container">
  <h2>📦 发布闲置物品</h2>

  <%-- 消息提示 --%>
  <%
    String error = (String)request.getAttribute("error");
    String success = (String)request.getAttribute("success");
    if(error != null) {
  %>
  <div class="error"><%= error %></div>
  <% } else if(success != null) { %>
  <div class="success"><%= success %></div>
  <% } %>

  <form action="publish" method="post" enctype="multipart/form-data">

    <!-- 使用 form-group (80% 居中) -->
    <div class="form-group">
      <label for="name">商品名称</label>
      <input type="text" id="name" name="product_name" class="form-control" required placeholder="如：9成新高等数学课本">
    </div>

    <div class="form-group">
      <label for="category">商品分类</label>
      <select id="category" name="category_id" class="form-control" required>
        <option value="">-- 请选择分类 --</option>
        <% for(Category cat : categoryList) { %>
        <option value="<%= cat.getCategoryId() %>"><%= cat.getCategoryName() %></option>
        <% } %>
      </select>
    </div>

    <div class="form-group">
      <label for="price">价格 (元)</label>
      <input type="number" id="price" name="price" class="form-control" step="0.01" min="0.01" required placeholder="如：15.00">
    </div>

    <div class="form-group">
      <label for="description">详细描述</label>
      <textarea id="description" name="description" class="form-control" required placeholder="描述一下商品的新旧程度、交易方式..."></textarea>
    </div>

    <div class="form-group">
      <label for="image">上传实物图</label>
      <div class="file-input-wrapper">
        <input type="file" id="image" name="product_image" accept="image/*" required>
      </div>
    </div>

    <!-- 使用 btn btn-primary (80% 宽度) -->
    <div class="form-group" style="text-align: center;">
      <button type="submit" class="btn btn-primary">确认发布</button>
    </div>
  </form>

  <div class="footer-link">
    <a href="index.jsp">返回首页</a>
  </div>
</div>


</body>
</html>