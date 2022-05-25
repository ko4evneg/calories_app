package com.github.ko4evneg.caloriesApp;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.model.Role;
import com.github.ko4evneg.caloriesApp.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class TestingData {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int NOT_FOUND_ID = 10;
    public static final int NEW_ID = 3;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);

    public static final int MEAL_USER_ONE_ID = 1;
    public static final int MEAL_USER_TWO_ID = 9;

    public static final Meal singleMeal = new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 10, 0), "Breakfast", 500, USER_ID);

    public static final List<Meal> meals = new ArrayList<>(List.of(
            singleMeal,
            new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 13, 0), "Dinner", 1000, USER_ID),
            new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 20, 0), "Supper", 500, USER_ID),
            new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 0, 0), "Edge case", 100, USER_ID),
            new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 1000, USER_ID),
            new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 500, USER_ID),
            new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 410, USER_ID),
            new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510, ADMIN_ID),
            new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500, ADMIN_ID)));
}
