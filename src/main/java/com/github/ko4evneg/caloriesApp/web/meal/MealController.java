package com.github.ko4evneg.caloriesApp.web.meal;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.service.MealService;
import com.github.ko4evneg.caloriesApp.to.MealTo;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import com.github.ko4evneg.caloriesApp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.util.DateTimeUtil.roundingConvertToEndDateTime;
import static com.github.ko4evneg.caloriesApp.util.DateTimeUtil.roundingConvertToStartDateTime;
import static com.github.ko4evneg.caloriesApp.util.SecurityUtil.authUserId;

@Controller
public class MealController {
    private static final Logger log = LoggerFactory.getLogger(MealController.class);

    private final MealService service;

    @Autowired
    public MealController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.debug("getAll");
        List<Meal> meals = service.getAll(authUserId());
        return MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
    }

    public MealTo get(Integer mealId) {
        log.debug("get {}", mealId);
        Meal meal = service.get(mealId, authUserId());
        return MealsUtil.mapFromMeal(meal);
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        log.debug("getBetween");
        int userId = SecurityUtil.authUserId();

        LocalDateTime startDateTime = roundingConvertToStartDateTime(startDate, startTime);
        LocalDateTime endDateTime = roundingConvertToEndDateTime(endDate, endTime);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDateTime, endDateTime, userId);

        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startDateTime, endDateTime);
    }

    public void delete(Integer mealId) {
        log.debug("delete {}", mealId);
        service.delete(mealId, authUserId());
    }

    public MealTo save(Meal meal) {
        log.debug("save {}", meal);
        Meal savedMeal = service.save(meal, authUserId());
        return MealsUtil.mapFromMeal(savedMeal);
    }
}