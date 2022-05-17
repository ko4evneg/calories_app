package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.service.MealService;
import com.github.ko4evneg.caloriesApp.service.MealServiceImpl;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import com.github.ko4evneg.caloriesApp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.github.ko4evneg.caloriesApp.util.MealsUtil.mapFromMeal;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private final MealService mealService = new MealServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action").toLowerCase();
        String mealId = Objects.requireNonNull(request.getParameter("id"));
        Integer userId = SecurityUtil.authUserId();

        switch (action) {
            case "delete" -> {
                log.debug("Delete meal {}", mealId);
                mealService.delete(Integer.parseInt(mealId), userId);
            }
            case "save" -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime datetime = LocalDateTime.parse(request.getParameter("datetime"), formatter);
                String description = request.getParameter("description");
                int calories = Integer.parseInt(request.getParameter("calories"));

                Meal meal = new Meal(
                        (mealId.isBlank() ? null : Integer.parseInt(mealId))
                        , datetime
                        , description
                        , calories
                        , userId);

                log.debug(meal.isNew() ? "Save new {}" : "Edit meal {}", meal);
                mealService.save(meal, userId);
            }
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = SecurityUtil.authUserId();

        if (request.getServletPath().equals("/meals")) {
            log.debug("getAll");
            request.setAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("WEB-INF/view/meals.jsp").forward(request, response);
        }

        if (request.getServletPath().equals("/meal")) {
            String mealId = request.getParameter("id");
            if (mealId == null) {
                log.debug("New meal creation");
                request.getRequestDispatcher("WEB-INF/view/mealSave.jsp").forward(request, response);
                return;
            }

            request.setAttribute("meal", mapFromMeal(mealService.get(Integer.parseInt(mealId), userId)));
            request.getRequestDispatcher("WEB-INF/view/mealSave.jsp").forward(request, response);
        }
    }
}
