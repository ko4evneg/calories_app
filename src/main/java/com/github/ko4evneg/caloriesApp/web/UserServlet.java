package com.github.ko4evneg.caloriesApp.web;

import javax.servlet.http.*;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("users.jsp");
    }

}
