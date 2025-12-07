<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html><html><head><meta charset="UTF-8"><title>校园二手交易 - 用户注册</title><!-- 引入外部 CSS 文件 --><link rel="stylesheet" href="css/main.css"><style>/* 继承 login-box 样式，但宽度略大，适应更多输入项 /.register-box {background: white;padding: 40px;border-radius: 8px;box-shadow: 0 4px 12px rgba(0,0,0,0.1);width: 400px; / 比登录框宽一点 */text-align: center;}.form-group-register {text-align: left;margin-bottom: 15px;}.form-group-register label {display: block;margin-bottom: 5px;font-weight: bold;color: #555;}</style></head><body><div class="register-box"><h2>🎉 注册新账号</h2><%-- 显示注册失败的错误提示或成功的提示 --%>
<%
    String error = (String)request.getAttribute("error");
    if(error != null){
%>
<div class="error"><%= error %></div>
<% } %>

<%
    String success = (String)request.getAttribute("success");
    if(success != null){
%>
<div class="success"><%= success %></div>
<div class="footer-link"><a href="login.jsp">立即登录</a></div>
<% } else { %>

<!-- 表单提交给 RegisterServlet (路径 /register, 稍后创建) -->
<form action="register" method="post">

    <div class="form-group-register">
        <label for="username">学号/账号 (必填)</label>
        <input type="text" id="username" name="username" placeholder="请输入您的学号，用于登录" required>
    </div>

    <div class="form-group-register">
        <label for="password">密码 (必填)</label>
        <input type="password" id="password" name="password" placeholder="请输入密码" required>
    </div>

    <div class="form-group-register">
        <label for="confirm_password">确认密码 (必填)</label>
        <input type="password" id="confirm_password" name="confirm_password" placeholder="请再次输入密码" required>
    </div>

    <div class="form-group-register">
        <label for="nickname">昵称 (必填)</label>
        <input type="text" id="nickname" name="nickname" placeholder="请输入您的昵称" required>
    </div>

    <div class="form-group-register">
        <label for="phone">联系电话 (可选)</label>
        <input type="text" id="phone" name="phone" placeholder="请输入您的手机号" >
    </div>

    <button type="submit" class="btn btn-primary">注 册</button>
</form>

<div class="footer-link">
    已有账号？<a href="login.jsp">返回登录</a>
</div>

<% } %>
</div></body></html>