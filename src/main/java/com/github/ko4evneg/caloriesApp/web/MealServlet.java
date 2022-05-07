package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.service.MealService;
import com.github.ko4evneg.caloriesApp.service.MealServiceImpl;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.github.ko4evneg.caloriesApp.util.MealsUtil.mapFromMeal;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private final MealService mealService = new MealServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/meals")) {
            log.debug("getAll");
            request.setAttribute("meals", MealsUtil.getTos(mealService.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("WEB-INF/view/meals.jsp").forward(request, response);
        }

        if (request.getServletPath().equals("/meal")) {
            String mealId = request.getParameter("id");
            if (mealId == null) {
                log.debug("New meal creation");
                request.getRequestDispatcher("WEB-INF/view/mealSave.jsp").forward(request, response);
                return;
            }

            Optional<Meal> mealOptional = mealService.get(Integer.parseInt(mealId));
            if (mealOptional.isEmpty()) {
                log.error("Meal id " + mealId + " not exist");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Meal id " + mealId + " was not found.");
            } else {
                request.setAttribute("meal", mapFromMeal(mealOptional.get()));
                request.getRequestDispatcher("WEB-INF/view/mealSave.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action").toLowerCase();
        String mealId = request.getParameter("id");

        switch (action) {
            case "delete" -> mealService.delete(Integer.parseInt(mealId));
            case "save" -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime datetime = LocalDateTime.parse(request.getParameter("datetime"), formatter);
                String description = request.getParameter("description");
                int calories = Integer.parseInt(request.getParameter("calories"));
                Meal meal = (mealId == null || mealId.isBlank()) ?
                        new Meal(datetime, description, calories) :
                        new Meal(Integer.parseInt(mealId), datetime, description, calories);
                mealService.save(meal);
            }
        }

        response.sendRedirect("meals");
    }
}
