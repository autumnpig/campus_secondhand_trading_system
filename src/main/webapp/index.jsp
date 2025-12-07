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
  List<Product> productList = null;

  // 1. 尝试获取搜索关键词
  String keyword = request.getParameter("keyword");

  // 2. 判断是搜索还是查看全部
  if (keyword != null && !keyword.trim().isEmpty()) {
    // 有关键词，执行搜索
    productList = productService.searchProducts(keyword.trim());
  } else {
    // 无关键词，获取所有在售商品
    productList = productService.getHomeProducts();
  }
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
    <div style="margin-top: 15px;">
      <a href="profile" class="btn btn-primary" style="width: auto; padding: 10px 30px; margin-right: 15px;">个人中心</a>

      <a href="myproducts" class="btn btn-primary" style="width: auto; padding: 10px 30px; margin-right: 15px;">我的商品</a>

      <a href="publish" class="btn btn-primary" style="width: auto; padding: 10px 30px;">发布闲置物品</a>
    </div>
  </div>

  <hr style="border: 0; border-top: 1px solid #eee; margin: 30px 0;">

  <div style="margin-bottom: 25px; text-align: center;">
    <form action="index.jsp" method="get" style="display: inline-block; width: 100%; max-width: 600px; display: flex;">
      <input type="text" name="keyword" class="form-control"
             placeholder="🔍 搜索你感兴趣的宝贝 (如：教材、耳机)"
             value="<%= keyword != null ? keyword : "" %>"
             style="margin: 0; border-top-right-radius: 0; border-bottom-right-radius: 0; flex: 1;">

      <button type="submit" class="btn btn-primary"
              style="width: 100px; margin: 0; border-top-left-radius: 0; border-bottom-left-radius: 0;">搜 索</button>

      <% if(keyword != null && !keyword.isEmpty()) { %>
      <a href="index.jsp" class="btn" style="width: 80px; margin-left: 10px; background: #ddd; color: #333; line-height: 44px; padding: 0;">清空</a>
      <% } %>
    </form>
  </div>

  <% if (keyword != null && !keyword.isEmpty()) { %>
  <h3 style="text-align: left; font-size: 18px; color: #666;">
    🔍 "<%= keyword %>" 的搜索结果 (<%= productList.size() %> 条)
  </h3>
  <% } else { %>
  <h3 style="text-align: left; padding-left: 10px; border-left: 4px solid #1890ff;">🔥 最新发布</h3>
  <% } %>

  <div class="product-grid">
    <%
      if(productList.isEmpty()) {
    %>
    <div class="empty-state">
      <p>暂无商品，快去发布第一个闲置吧！</p>
      <a href="publish" class="btn btn-primary" style="width: auto; padding: 10px 30px;">去发布</a>
    </div>
    <%
    } else {
      for(Product p : productList) {
    %>
    <a href="detail?id=<%= p.getProductId() %>" class="product-card">
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
  <a href="${pageContext.request.contextPath}/logout" class="btn btn-logout">安全退出</a>
</div>

</body>
</html>