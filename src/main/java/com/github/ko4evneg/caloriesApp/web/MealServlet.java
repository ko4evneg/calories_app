package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.SpringMain;
import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.service.MealService;
import com.github.ko4evneg.caloriesApp.util.DateTimeUtil;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import com.github.ko4evneg.caloriesApp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.github.ko4evneg.caloriesApp.util.MealsUtil.mapFromMeal;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private final MealService mealService = SpringMain.getContext().getBean(MealService.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action").toLowerCase();
        Integer userId = SecurityUtil.authUserId();

        switch (action) {
            case "delete" -> {
                String mealId = Objects.requireNonNull(request.getParameter("id"));
                log.debug("Delete meal {}", mealId);
                mealService.delete(Integer.parseInt(mealId), userId);
            }
            case "save" -> {
                String mealId = Objects.requireNonNull(request.getParameter("id"));
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
            case "filter" -> {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                String startDateString = request.getParameter("startDate");
                String startTimeString = request.getParameter("startTime");
                String endDateString = request.getParameter("endDate");
                String endTimeString = request.getParameter("endTime");
                LocalDateTime startDateTime = DateTimeUtil.roundingConvertToStartDateTime(
                        startDateString == null || startDateString.isBlank() ? null : LocalDate.parse(startDateString, dateFormatter),
                        startTimeString == null || startTimeString.isBlank() ? null : LocalTime.parse(startTimeString, timeFormatter));
                LocalDateTime endDateTime = DateTimeUtil.roundingConvertToEndDateTime(
                        startDateString == null || endDateString.isBlank() ? null : LocalDate.parse(endDateString, dateFormatter),
                        startTimeString == null || endTimeString.isBlank() ? null : LocalTime.parse(endTimeString, timeFormatter));

                log.debug("Filter all meals");
                request.setAttribute("meals",
                        MealsUtil.getTos(mealService.getBetweenInclusive(startDateTime, endDateTime, userId), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("WEB-INF/view/meals.jsp").forward(request, response);
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
