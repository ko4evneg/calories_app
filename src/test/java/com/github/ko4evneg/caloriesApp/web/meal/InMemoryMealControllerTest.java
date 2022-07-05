package com.github.ko4evneg.caloriesApp.web.meal;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryMealRepository;
import com.github.ko4evneg.caloriesApp.to.MealTo;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;
import static org.junit.Assert.*;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:db/inmemory-db.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("inMemory")
public class InMemoryMealControllerTest {
    @Autowired
    private MealController controller;
    @Autowired
    private MealRepository repository;

    @Before
    public void setUp() {
        if (repository instanceof InMemoryMealRepository) {
            ((InMemoryMealRepository) repository).init();
        }
    }

    @Test
    public void deleteExisting() {
        assertNotNull(repository.get(USERS_MEAL_ID, USER_ID));
        controller.delete(USERS_MEAL_ID);
        assertNull(repository.get(USERS_MEAL_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        controller.delete(NOT_FOUND_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongUser() {
        assertNotNull(repository.get(ADMINS_MEAL_ID, ADMIN_ID));
        controller.delete(ADMINS_MEAL_ID);
        assertNotNull((repository.get(ADMINS_MEAL_ID, ADMIN_ID)));
    }

    @Test
    public void getExisting() {
        MealTo actualMeal = controller.get(USERS_MEAL_ID);
        assertEquals(MealsUtil.mapFromMeal(singleMeal), actualMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongUser() {
        controller.get(ADMINS_MEAL_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        controller.get(NOT_FOUND_ID);
    }

    @Test
    public void getAllFilled() {
        List<MealTo> actualMeals = controller.getAll();
        assertEquals(getExpectedUserOneMeals(), actualMeals);
    }

    private List<MealTo> getExpectedUserOneMeals() {
        return MealsUtil.getTos(meals.stream()
                .filter(m -> m.getUserId().equals(USER_ID))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .toList(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    @Test
    public void createNew() {
        MealTo expectedMeal = MealsUtil.mapFromMeal(new Meal(NEW_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID));
        Meal meal = new Meal(null, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);

        MealTo createdMeal = controller.save(meal);
        MealTo actualMeal = MealsUtil.mapFromMeal(repository.get(NEW_MEAL_ID, USER_ID));

        assertEquals(expectedMeal, createdMeal);
        assertEquals(expectedMeal, actualMeal);
    }

    @Test(expected = NotFoundException.class)
    public void createOrEditAlienFail() {
        Meal meal = new Meal(ADMINS_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);
        controller.save(meal);
    }

    @Test
    public void editSelf() {
        Meal meal = new Meal(USERS_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);
        controller.save(meal);
        assertEquals(MealsUtil.mapFromMeal(meal), controller.get(meal.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void editNotExistingFail() {
        Meal meal = new Meal(NEW_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);
        controller.save(meal);
    }
}