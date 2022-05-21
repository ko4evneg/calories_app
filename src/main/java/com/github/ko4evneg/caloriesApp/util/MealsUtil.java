package com.github.ko4evneg.caloriesApp.util;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository.USER_ID;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> meals = new ArrayList<>();

    static {
        meals.add(new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 10, 0), "Breakfast", 500, USER_ID));
        meals.add(new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 13, 0), "Dinner", 1000, USER_ID));
        meals.add(new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 20, 0), "Supper", 500, USER_ID));
        meals.add(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 0, 0), "Edge case", 100, USER_ID));
        meals.add(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 1000, USER_ID));
        meals.add(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 500, USER_ID));
        meals.add(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 410, USER_ID));
    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> Util.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime));
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> dailyCalories = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));

        return meals.stream()
                .filter(filter)
                .map(meal -> mapFromMeal(meal, dailyCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    //TODO: add mapper
    private static MealTo mapFromMeal(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess, meal.getUserId());
    }

    public static MealTo mapFromMeal(Meal meal) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getUserId());
    }
    }
