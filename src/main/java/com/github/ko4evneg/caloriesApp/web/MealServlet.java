package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.web.meal.MealRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
    private static final MealRestController controller = context.getBean(MealRestController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action").toLowerCase();
        String mealId = Objects.requireNonNull(request.getParameter("id"));

        switch (action) {
            case "delete" -> {
                log.debug("Delete meal {}", mealId);
                controller.delete(Integer.parseInt(mealId));
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
                        , 1);

                log.debug(meal.isNew() ? "Save new {}" : "Edit meal {}", meal);
                controller.save(meal);
            }
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/meals")) {
            log.debug("getAll");
            request.setAttribute("meals", controller.getAll());
            request.getRequestDispatcher("WEB-INF/view/meals.jsp").forward(request, response);
        }

        if (request.getServletPath().equals("/meal")) {
            String mealId = request.getParameter("id");
            if (mealId == null) {
                log.debug("New meal creation");
                request.getRequestDispatcher("WEB-INF/view/mealSave.jsp").forward(request, response);
                return;
            }

            request.setAttribute("meal", controller.get(Integer.parseInt(mealId)));
            request.getRequestDispatcher("WEB-INF/view/mealSave.jsp").forward(request, response);
        }
    }
}
