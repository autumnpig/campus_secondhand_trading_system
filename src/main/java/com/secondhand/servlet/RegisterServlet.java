package com.secondhand.servlet;

import com.secondhand.dao.UserDao;
import com.secondhand.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户注册请求处理 Servlet
 * 映射路径: /register
 * 负责接收注册表单，进行校验和用户创建。
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 1. 获取所有表单参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String nickname = request.getParameter("nickname");
        String phone = request.getParameter("phone");
        // address 暂时留空，role 默认为 0

        // 2. 基础校验 (非空检查已在 JSP required 属性中处理，这里进行二次校验)
        if (username.trim().isEmpty() || password.trim().isEmpty() || nickname.trim().isEmpty()) {
            request.setAttribute("error", "账号、密码和昵称不能为空！");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 3. 校验两次密码是否一致
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不一致！");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 4. 校验用户名（学号）是否已被注册
        if (userDao.checkUsernameExist(username)) {
            request.setAttribute("error", "该学号已被注册，请直接登录或联系管理员。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 5. 封装 User 实体
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setNickname(nickname);
        newUser.setPhone(phone);
        newUser.setAddress(""); // 注册时地址可为空
        newUser.setRole(0); // 默认注册为普通用户 (0)

        // --- 调试日志：检查是否获取到正确数据 ---
        System.out.println("--- RegisterServlet Debug ---");
        System.out.println("Attempting to register user: " + username);
        System.out.println("Nickname: " + nickname);
        System.out.println("Phone: " + (phone.isEmpty() ? "None" : phone));
        System.out.println("Role: 0 (Default)");
        System.out.println("---------------------------");
        // ----------------------------------------

        // 6. 调用 DAO 进行注册
        boolean success = userDao.register(newUser);

        // 7. 处理注册结果
        if (success) {
            request.setAttribute("success", "恭喜您，注册成功！请使用新账号登录。");
            // 注册成功后，不使用重定向，而是转发回注册页显示成功信息，并提供登录链接
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "注册失败，请稍后重试或检查数据库连接配置。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    // 处理 GET 请求，直接转发到注册页面
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}