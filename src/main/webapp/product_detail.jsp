<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.secondhand.entity.Product" %>
<%@ page import="com.secondhand.entity.User" %>
<%
  // 从 Request 作用域获取数据
  Product product = (Product) request.getAttribute("product");
  User seller = (User) request.getAttribute("seller");

  if (product == null) {
    // 如果没有商品数据，应该已经在 Servlet 中处理并转发了，此处是二次保险
    response.sendRedirect("index.jsp?error=notfound");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title><%= product.getProductName() %> - 商品详情</title>
  <link rel="stylesheet" href="css/main.css">
  <style>
    /* 针对详情页的特殊样式 */
    .detail-layout {
      display: flex;
      gap: 30px;
      text-align: left;
      margin-top: 30px;
      flex-wrap: wrap;
    }
    .product-image-area {
      flex: 1;
      min-width: 300px;
      max-width: 450px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      display: flex;
      align-items: center; /* 垂直居中 */
      justify-content: center; /* 水平居中 */
    }
    .product-image-area img {
      width: 100%;
      height: auto;
      object-fit: contain; /* 完整显示图片 */
      display: block;
      background-color: #f9f9f9;
    }
    .product-info-area {
      flex: 2;
      min-width: 300px;
    }
    .product-price-large {
      color: #e74c3c;
      font-weight: bold;
      font-size: 32px;
      margin: 15px 0 25px 0;
      border-bottom: 1px solid #eee;
      padding-bottom: 15px;
    }
    .info-block {
      margin-bottom: 20px;
    }
    .info-block h4 {
      font-size: 18px;
      color: #555;
      border-left: 3px solid #1890ff;
      padding-left: 10px;
      margin-bottom: 10px;
      text-align: left;
    }
    .info-block p {
      line-height: 1.8;
      color: #666;
      white-space: pre-wrap; /* 保持描述中的换行符 */
    }
    .seller-contact {
      background: #f0f8ff;
      padding: 15px;
      border-radius: 6px;
      border: 1px solid #d0e9ff;
      margin-top: 20px;
    }
    .status-badge {
      display: inline-block;
      padding: 4px 10px;
      border-radius: 4px;
      font-size: 14px;
      font-weight: bold;
    }
    .status-sold { background-color: #e74c3c; color: white; }
    .status-onsale { background-color: #2ecc71; color: white; }
  </style>
</head>
<body>

<div class="content-container">
  <h1>商品详情</h1>

  <div style="text-align: left; margin-bottom: 20px;">
    <% if (product.getStatus() == 1) { %>
    <span class="status-badge status-sold">已售出</span>
    <% } else { %>
    <span class="status-badge status-onsale">在售中</span>
    <% } %>
  </div>


  <div class="detail-layout">
    <div class="product-image-area">
      <img src="<%= product.getImageUrl() %>" alt="<%= product.getProductName() %>" onerror="this.src='images/placeholder.png'">
    </div>

    <div class="product-info-area">

      <div class="info-block">
        <h2><%= product.getProductName() %></h2>
        <div class="product-price-large">
          ¥ <%= product.getPrice() %>
        </div>
      </div>

      <div class="info-block">
        <h4>详细描述</h4>
        <p><%= product.getDescription() %></p>
      </div>

      <div class="info-block">
        <h4>发布信息</h4>
        <p>
          发布时间: <%= product.getPublishTime() != null ? product.getPublishTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A" %> <br>
          商品分类ID: <%= product.getCategoryId() %>
        </p>
      </div>

      <div class="info-block seller-contact">
        <h4>📢 卖家联系方式 (请线下沟通交易细节)</h4>
        <% if (seller != null) { %>
        <p>
          **卖家昵称**: <%= seller.getNickname() %> <br>
          **学号**: <%= seller.getUsername() %> <br>
          **联系电话**: <%= seller.getPhone() != null && !seller.getPhone().isEmpty() ? seller.getPhone() : "未提供" %> <br>
          **默认交易地址**: <%= seller.getAddress() != null && !seller.getAddress().isEmpty() ? seller.getAddress() : "未设置默认地址" %>
        </p>
        <% } else { %>
        <p style="color: red;">卖家信息查询失败，请稍后重试。</p>
        <% } %>
      </div>

      <div style="margin-top: 30px; text-align: center;">
        <a href="index.jsp" class="btn btn-primary" style="width: 200px;">返回商品列表</a>
      </div>

    </div>
  </div>
</div>

</body>
</html>