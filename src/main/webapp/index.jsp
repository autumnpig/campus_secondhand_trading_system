<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.secondhand.entity.User" %>
<%@ page import="com.secondhand.entity.Product" %>
<%@ page import="com.secondhand.service.ProductService" %>
<%@ page import="java.util.List" %>
<%
  // 权限拦截
  User user = (User) session.getAttribute("currentUser");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }

// 获取商品列表
  ProductService productService = new ProductService();
  List<Product> productList = productService.getHomeProducts();


%>

<!DOCTYPE html>

<html>
<head>
  <meta charset="UTF-8">
  <title>校园二手交易 - 首页</title>
  <link rel="stylesheet" href="css/main.css">
</head>
<body>

<div class="content-container">
  <h1>🎉 欢迎回来, <%= user.getNickname() %> 同学！</h1>

  <div style="margin-bottom: 30px;">
    <p>你的身份：普通用户 | 学号：<%= user.getUsername() %></p>
    <!-- 导航按钮组 -->
    <div style="margin-top: 15px;">
      <a href="profile" class="btn btn-primary" style="width: auto; padding: 10px 30px; margin-right: 15px;">个人中心</a>

      <a href="myproducts" class="btn btn-primary" style="width: auto; padding: 10px 30px; margin-right: 15px;">我的商品</a>

      <a href="publish" class="btn btn-primary" style="width: auto; padding: 10px 30px;">发布闲置物品</a>
    </div>
  </div>

  <hr style="border: 0; border-top: 1px solid #eee; margin: 30px 0;">

  <h3 style="text-align: left; padding-left: 10px; border-left: 4px solid #1890ff;">🔥 最新发布</h3>

  <!-- 商品列表网格 -->
  <div class="product-grid">
    <%
      if(productList.isEmpty()) {
    %>
    <div class="empty-state">
      <p>暂无商品，快去发布第一个闲置吧！</p>
      <!-- 移除 style 属性 -->
      <a href="publish" class="btn btn-primary" style="width: auto; padding: 10px 30px;">去发布</a>
    </div>
    <%
    } else {
      for(Product p : productList) {
    %>
    <a href="detail?id=<%= p.getProductId() %>" class="product-card">
      <!-- 确保 img 标签有 product-img 类 -->
      <img src="<%= p.getImageUrl() %>" alt="<%= p.getProductName() %>" class="product-img" onerror="this.src='images/placeholder.png'">

      <div class="product-info">
        <div class="product-title"><%= p.getProductName() %></div>
        <div class="product-price">¥ <%= p.getPrice() %></div>
        <div class="product-meta">
          <span><%= p.getDescription().length() > 10 ? p.getDescription().substring(0, 10) + "..." : p.getDescription() %></span>
        </div>
      </div>
    </a>
    <%
        }
      }
    %>
  </div>

  <br>
  <!-- 退出登录按钮 -->
  <a href="${pageContext.request.contextPath}/logout" class="btn btn-logout">安全退出</a>
</div>


</body>
</html>