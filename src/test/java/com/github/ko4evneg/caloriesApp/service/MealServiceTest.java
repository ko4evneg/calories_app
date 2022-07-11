package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.util.MealAssertionsHelper;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.ko4evneg.caloriesApp.TestingData.*;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"jpa"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;
    @Autowired
    private MealAssertionsHelper assertionsHelper;

    @Test
    public void get() {
        Meal actualMeal = mealService.get(USERS_MEAL_ID, USER_ID);
        assertionsHelper.assertRecursiveEquals(actualMeal, singleMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongUser() {
        mealService.get(ADMINS_MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        mealService.get(NOT_FOUND_ID, USER_ID);
    }

    @Test
    public void getAll() {
        List<Meal> actualMeals = mealService.getAll(USER_ID);
        List<Meal> expectedMeals = getUserOneSortedMeals();
        assertionsHelper.assertAllRecursiveEquals(actualMeals, expectedMeals);
    }

    private List<Meal> getUserOneSortedMeals() {
        return meals.stream()
                .filter(m -> m.getUser().getId().equals(USER_ID))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Test
    public void getBetweenInclusive() {
        LocalDateTime startDateTime = LocalDateTime.of(2022, Month.APRIL, 29, 13, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, Month.APRIL, 30, 13, 0);

        List<Meal> expectedMeals = getUserOneSortedMeals().stream()
                .filter(m -> m.getDateTime().compareTo(startDateTime) >= 0 && m.getDateTime().compareTo(endDateTime) < 0)
                .collect(Collectors.toList());

        List<Meal> actualMeals = mealService.getBetweenInclusive(startDateTime, endDateTime, USER_ID);

        assertionsHelper.assertAllRecursiveEquals(actualMeals, expectedMeals);
    }

    @Test
    public void saveOwned() {
        Meal expectedMeal = new Meal(LocalDateTime.of(2022, Month.JULY, 10, 19, 20, 0), "test meal", 1100, getUserWithId(ADMIN_ID));
        mealService.save(expectedMeal, ADMIN_ID);

        Meal actualMeal = mealService.get(NEW_MEAL_ID, ADMIN_ID);
        assertionsHelper.assertRecursiveEquals(actualMeal, expectedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void createOrEditAlienFail() {
        Meal meal = new Meal(ADMINS_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, getUserWithId(USER_ID));
        mealService.save(meal, USER_ID);
    }

    @Test
    public void editOwned() {
        Meal expectedMeal = new Meal(ADMINS_MEAL_ID, LocalDateTime.of(2022, Month.JULY, 10, 19, 20, 0), "test meal", 1100, getUserWithId(ADMIN_ID));
        mealService.save(expectedMeal, ADMIN_ID);

        Meal actualMeal = mealService.get(ADMINS_MEAL_ID, ADMIN_ID);
        assertionsHelper.assertRecursiveEquals(actualMeal, expectedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void editNotExistingFail() {
        Meal meal = new Meal(NEW_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, getUserWithId(USER_ID));
        mealService.save(meal, USER_ID);
    }

    @Test
    public void delete() {
        mealService.delete(USERS_MEAL_ID, USER_ID);

        List<Meal> expectedMeals = getUserOneSortedMeals();
        expectedMeals.remove(singleMeal);

        assertionsHelper.assertAllRecursiveEquals(mealService.getAll(USER_ID), expectedMeals);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        mealService.delete(NOT_FOUND_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongUser() {
        assertNotNull(mealService.get(ADMINS_MEAL_ID, ADMIN_ID));
        mealService.delete(ADMINS_MEAL_ID, USER_ID);
        assertNotNull((mealService.get(ADMINS_MEAL_ID, ADMIN_ID)));
    }
}
