package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.model.MealTo;
import com.github.ko4evneg.caloriesApp.service.MealService;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final int CALORIES_LIMIT = 2000;

    private final MealService mealService = new MealService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> meals = MealsUtil.filteredByStreams(mealService.getMeals(),
                LocalDateTime.MIN.toLocalTime(), LocalDateTime.MAX.toLocalTime(), CALORIES_LIMIT);

            request.setAttribute("meals", meals);
            request.getRequestDispatcher("WEB-INF/view/meals.jsp").forward(request, response);

            log.info("redirect: meals.jsp");
    }
}
