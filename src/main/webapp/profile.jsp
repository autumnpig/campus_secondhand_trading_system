<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.secondhand.entity.User" %>
<%
  // 权限拦截：确保用户已登录
  User user = (User) session.getAttribute("currentUser");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  // 获取提示信息
  String error = (String)request.getAttribute("error");
  String success = (String)request.getAttribute("success");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>个人信息管理</title>
  <link rel="stylesheet" href="css/main.css">
  <style>
    .profile-container {
      width: 500px;
      max-width: 90%;
      margin: 80px auto;
      padding: 30px;
      background: white;
      border-radius: 10px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
      text-align: left;
    }
    .profile-info-display {
      margin-bottom: 20px;
      padding: 10px;
      border-bottom: 1px solid #eee;
    }
    .profile-info-display p {
      margin: 8px 0;
    }
  </style>
</head>
<body>

<div class="profile-container">
  <h2>👤 个人信息管理</h2>

  <%-- 消息提示 --%>
  <% if(error != null) { %>
  <div class="error"><%= error %></div>
  <% } else if(success != null) { %>
  <div class="success"><%= success %></div>
  <% } %>

  <div class="profile-info-display">
    <p><strong>学号/账号:</strong> <%= user.getUsername() %></p>
    <p><strong>身份:</strong> <%= user.getRole() == 1 ? "管理员" : "普通用户" %></p>
    <p><strong>注册时间:</strong> <%= user.getCreateTime() != null ? user.getCreateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A" %></p>
  </div>

  <form action="profile" method="post">

    <div class="form-group">
      <label for="nickname">用户昵称 (必填)</label>
      <input type="text" id="nickname" name="nickname" class="form-control"
             value="<%= user.getNickname() != null ? user.getNickname() : "" %>" required>
    </div>

    <div class="form-group">
      <label for="phone">联系电话</label>
      <input type="text" id="phone" name="phone" class="form-control"
             value="<%= user.getPhone() != null ? user.getPhone() : "" %>">
      <small style="color: #999;">用于买家/卖家联系，请确保准确。</small>
    </div>

    <div class="form-group">
      <label for="address">默认交易/收货地址</label>
      <textarea id="address" name="address" class="form-control" rows="3"><%= user.getAddress() != null ? user.getAddress() : "" %></textarea>
      <small style="color: #999;">例如：南京信息工程大学XX宿舍楼</small>
    </div>

    <div class="form-group" style="text-align: center; margin-top: 25px;">
      <button type="submit" class="btn btn-primary" style="width: 100%;">保存修改</button>
    </div>
  </form>

  <div class="footer-link" style="text-align: center;">
    <a href="index.jsp">返回首页</a>
  </div>
</div>

</body>
</html>