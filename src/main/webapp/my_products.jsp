<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.secondhand.entity.User" %>
<%@ page import="com.secondhand.entity.Product" %>
<%@ page import="java.util.List" %>
<%
    // 权限拦截和数据获取（与 Servlet 配合）
    User user = (User) session.getAttribute("currentUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Product> productList = (List<Product>) request.getAttribute("productList");
    String error = (String)request.getAttribute("error");
    String success = (String)request.getAttribute("success");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的商品管理 - <%= user.getNickname() %></title>
    <link rel="stylesheet" href="css/main.css">
    <style>
        /* 新增样式 */
        .product-list-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .product-list-table th, .product-list-table td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }
        /* ... (CSS 样式已在我的 thought 中提供，此处省略以保持简洁) ... */
        .product-list-table img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
        }
        .status-badge-onsale { color: #2ecc71; font-weight: bold; }
        .status-badge-sold { color: #e74c3c; font-weight: bold; }
        .action-link { display: inline-block; margin: 0 5px; color: #1890ff; text-decoration: none; }
        .action-delete { color: #e74c3c; }
    </style>
</head>
<body>

<div class="content-container">
    <h1>📦 我的闲置物品管理</h1>
    <p>当前登录用户：<%= user.getNickname() %></p>

    <%-- 消息提示 --%>
    <% if(error != null) { %>
    <div class="error"><%= error %></div>
    <% } else if(success != null) { %>
    <div class="success"><%= success %></div>
    <% } %>

    <% if (productList.isEmpty()) { %>
    <div style="padding: 50px; text-align: center; color: #999;">
        <p>您还没有发布任何商品。</p>
        <a href="publish" class="btn btn-primary" style="width: auto; padding: 10px 30px; margin-top: 15px;">立即发布</a>
    </div>
    <% } else { %>

    <table class="product-list-table">
        <thead>
        <tr>
            <th>图片</th>
            <th>名称</th>
            <th>价格 (¥)</th>
            <th>状态</th>
            <th>发布时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <% for(Product p : productList) { %>
        <tr>
            <td><img src="<%= p.getImageUrl() %>" alt="<%= p.getProductName() %>" onerror="this.src='images/placeholder.png'"></td>
            <td><a href="detail?id=<%= p.getProductId() %>"><%= p.getProductName() %></a></td>
            <td><%= p.getPrice() %></td>
            <td>
                <% if (p.getStatus() == 0) { %>
                <span class="status-badge-onsale">在售中</span>
                <% } else { %>
                <span class="status-badge-sold">已售出</span>
                <% } %>
            </td>
            <td><%= p.getPublishTime() != null ? p.getPublishTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A" %></td>
            <td>
                <a href="edit_product?id=<%= p.getProductId() %>" class="action-link">编辑</a>

                <%-- 标记已售/重新上架 --%>
                <% if (p.getStatus() == 0) { %>
                <a href="myproducts?action=mark_sold&id=<%= p.getProductId() %>" class="action-link"
                   onclick="return confirm('确定要将商品 [<%= p.getProductName() %>] 标记为已售出吗？');">标记已售</a>
                <% } else { %>
                <span style="color: #999;">已售</span>
                <% } %>

                <%-- 删除功能 --%>
                <a href="myproducts?action=delete&id=<%= p.getProductId() %>" class="action-link action-delete"
                   onclick="return confirm('警告：确定要删除商品 [<%= p.getProductName() %>] 吗？删除后不可恢复。');">删除</a>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <% } %>

    <br>
    <div class="footer-link">
        <a href="profile">返回个人中心</a> |
        <a href="index.jsp">返回首页</a>
    </div>

</div>

</body>
</html>