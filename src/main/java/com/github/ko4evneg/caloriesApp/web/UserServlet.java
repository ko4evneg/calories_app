package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.SpringMain;
import com.github.ko4evneg.caloriesApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private final UserService service = SpringMain.getContext().getBean(UserService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("redirect: users.jsp");
        //TODO Move to users dto
        request.setAttribute("users", service.getAll());
        request.getRequestDispatcher("WEB-INF/view/users.jsp").forward(request, response);
    }
}
