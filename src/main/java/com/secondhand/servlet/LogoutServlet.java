package com.secondhand.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 退出登录 Servlet
 * 映射路径: /logout
 * 功能: 销毁 Session，实现安全退出
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取当前 Session
        // 参数 false 表示：如果 Session 不存在，不要创建一个新的（反正都要退出了）
        HttpSession session = request.getSession(false);

        // 2. 如果 Session 存在，则使其失效 (销毁 Session)
        // 这一步是退出的关键，清除了服务器端存储的用户信息
        if (session != null) {
            session.invalidate();
        }

        // 3. 退出后重定向回登录页面
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 无论是点链接(GET)还是提交表单(POST)退出，处理逻辑都一样
        doGet(request, response);
    }
}