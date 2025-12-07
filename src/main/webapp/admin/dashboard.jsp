<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.secondhand.entity.User" %>
<%
    // 权限拦截：未登录或不是管理员，踢回登录页
    User user = (User) session.getAttribute("currentUser");
    if (user == null || user.getRole() != 1) {
// 因为在 admin 文件夹里，跳回登录页需要 ../
        response.sendRedirect("../login.jsp");
        return;
    }
%>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>后台管理系统</title>
    <!-- 引入外部 CSS 文件 (使用 ../ 返回上一级目录找到 css/main.css) -->
    <link rel="stylesheet" href="../css/main.css">
</head>
<body>
<div class="content-container">
    <h1>管理员控制台</h1>
    <p>欢迎管理员：<%= user.getNickname() %></p>

    <hr>
    <h3>管理功能</h3>
    <ul>
        <li><a href="#">用户管理 (待开发)</a></li>
        <li><a href="#">商品审核 (待开发)</a></li>
        <li><a href="#">分类管理 (待开发)</a></li>
    </ul>
    <!-- 退出链接，使用相对路径 -->
    <a href="../logout" class="btn btn-logout">安全退出</a>
</div>


</body>
</html>