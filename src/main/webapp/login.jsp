<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>校园二手交易 - 登录</title>
    <!-- 引入外部 CSS 文件 -->
    <link rel="stylesheet" href="css/main.css">
</head>
<body>

<div class="login-box">
    <h2>👋 欢迎登录</h2>

    <%-- 1. 显示登录失败的错误提示 --%>
    <%
        String error = (String)request.getAttribute("error");
        if(error != null){
    %>
    <div class="error"><%= error %></div>
    <% } %>

    <%-- 2. 显示退出成功的提示 (LogoutServlet 会带参数 ?msg=logout 跳转过来) --%>
    <%
        String msg = request.getParameter("msg");
        if("logout".equals(msg)){
    %>
    <div class="success">✅ 您已安全退出系统</div>
    <% } %>

    <!-- 表单提交给 LoginServlet (路径 /login) -->
    <form action="login" method="post">
        <input type="text" name="username" placeholder="请输入学号/工号" required>
        <input type="password" name="password" placeholder="请输入密码" required>
        <button type="submit" class="btn btn-primary">登 录</button>
    </form>

    <div class="footer-link">
        还没有账号？<a href="register.jsp">立即注册</a>
    </div>


</div>

</body>
</html>