package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.service.UserService;
import com.github.ko4evneg.caloriesApp.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("redirect: users.jsp");
        request.setAttribute("users", service.getAll());
        request.getRequestDispatcher("WEB-INF/view/users.jsp").forward(request, response);
    }
}
