package com.github.ko4evneg.caloriesApp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("getAll");
        request.setAttribute("meals", MealsUtil.getTos(MealsUtil.meals, MealsUtil.DEFAULT_CALORIES_PER_DAY));
        request.getRequestDispatcher("WEB-INF/view/meals.jsp").forward(request, response);
    }
}
