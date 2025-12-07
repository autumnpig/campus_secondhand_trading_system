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
 * 个人信息管理 Servlet
 * 映射路径: /profile
 * 负责显示用户信息和处理用户信息更新。
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 权限拦截
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // GET 请求：显示 profile.jsp 页面
        // 因为 currentUser 已经在 Session 中，直接转发即可
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 权限拦截
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 1. 获取表单参数
        String nickname = request.getParameter("nickname");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // 2. 基础校验 (昵称不能为空)
        if (nickname == null || nickname.trim().isEmpty()) {
            request.setAttribute("error", "昵称不能为空！");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }

        // 3. 封装待更新的 User 实体
        User userToUpdate = new User();
        userToUpdate.setUserId(currentUser.getUserId()); // 必须设置 ID 用于 WHERE 子句
        userToUpdate.setNickname(nickname);
        userToUpdate.setPhone(phone != null ? phone : "");
        userToUpdate.setAddress(address != null ? address : "");

        // 4. 调用 DAO 更新数据库
        boolean success = userDao.updateUserInfo(userToUpdate);

        // 5. 处理结果
        if (success) {
            // 更新成功后，必须同步更新 Session 中的用户信息，以确保页面显示最新数据
            currentUser.setNickname(userToUpdate.getNickname());
            currentUser.setPhone(userToUpdate.getPhone());
            currentUser.setAddress(userToUpdate.getAddress());
            session.setAttribute("currentUser", currentUser);

            request.setAttribute("success", "个人信息更新成功！");
        } else {
            request.setAttribute("error", "信息更新失败，请稍后重试。");
        }

        // 转发回 profile.jsp 显示结果
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
}