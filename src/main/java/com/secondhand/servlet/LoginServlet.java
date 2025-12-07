package com.secondhand.servlet;

import com.secondhand.dao.UserDao;
import com.secondhand.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录请求处理 Servlet
 * 映射路径: /login
 * 对应报告: 3.3.1 用户登录流程
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // 实例化 DAO 对象，用于后续的数据库操作
    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 设置请求和响应的编码，防止中文乱码
        request.setCharacterEncoding("UTF-8");

        // 2. 获取前端表单提交的参数
        String username = request.getParameter("username"); // 对应 login.jsp 中的 name="username"
        String password = request.getParameter("password"); // 对应 login.jsp 中的 name="password"

        // 3. 后端非空校验 (对应报告流程第一步)
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "账号或密码不能为空！");
            // 校验失败，转发回登录页面显示错误
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 4. 调用业务层/DAO层进行身份验证 (对应报告流程第三步)
        // 去数据库查这个账号密码对不对
        User user = userDao.login(username, password);

        // 5. 根据验证结果进行处理
        if (user != null) {
            // --- 登录成功 ---

            // A. 创建 Session 并保存用户信息 (对应报告流程第五步)
            // 这样系统就知道"当前是谁在登录"
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);

            // B. 根据用户角色 (Role) 重定向到不同页面 (对应报告流程第六步)
            if (user.getRole() == 1) {
                // 如果是管理员 (role=1)，跳转到后台管理页
                System.out.println("管理员登录成功: " + user.getNickname());
                // 注意：你需要确保 webapp/admin/dashboard.jsp 存在，否则会报404
                response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            } else {
                // 如果是普通用户 (role=0)，跳转到前台首页
                System.out.println("用户登录成功: " + user.getNickname());
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            // --- 登录失败 ---
            request.setAttribute("error", "账号或密码错误，请重新输入！");
            // 转发回登录页
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    // 处理 GET 请求：如果用户直接在浏览器输入 /login，也显示登录页面
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}