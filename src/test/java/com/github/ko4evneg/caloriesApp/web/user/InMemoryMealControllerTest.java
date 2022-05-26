package com.github.ko4evneg.caloriesApp.web.user;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryMealRepository;
import com.github.ko4evneg.caloriesApp.to.MealTo;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import com.github.ko4evneg.caloriesApp.web.meal.MealController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class InMemoryMealControllerTest {
    @Autowired
    private MealController controller;
    @Autowired
    private InMemoryMealRepository repository;

    @Before
    public void setUp() {
        repository.init();
    }

    //TODO: add auth to all methods

    @Test
    public void deleteExisting() {
        controller.delete(MEAL_USER_ONE_ID);
        assertTrue(repository.get(MEAL_USER_ONE_ID, USER_ID).isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        controller.delete(NOT_FOUND_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongUser() {
        assertTrue(repository.get(MEAL_USER_TWO_ID, ADMIN_ID).isPresent());
        controller.delete(MEAL_USER_TWO_ID);
        assertTrue(repository.get(MEAL_USER_TWO_ID, ADMIN_ID).isPresent());
    }

    @Test
    public void getExisting() {
        MealTo actualMeal = controller.get(MEAL_USER_ONE_ID);
        assertEquals(MealsUtil.mapFromMeal(singleMeal), actualMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongUser() {
        controller.get(MEAL_USER_TWO_ID);
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
                .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                .toList(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    @Test
    public void createNew() {
        MealTo expectedMeal = MealsUtil.mapFromMeal(new Meal(NEW_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID));
        Meal meal = new Meal(null, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);

        MealTo createdMeal = controller.save(meal);
        MealTo actualMeal = controller.get(NEW_MEAL_ID);

        assertEquals(expectedMeal, createdMeal);
        assertEquals(expectedMeal, actualMeal);
    }

    @Test(expected = NotFoundException.class)
    public void createOrEditAlienFail() {
        Meal meal = new Meal(MEAL_USER_TWO_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);
        controller.save(meal);
    }

    @Test
    public void editSelf() {
        Meal meal = new Meal(MEAL_USER_ONE_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);
        controller.save(meal);
        assertEquals(MealsUtil.mapFromMeal(meal), controller.get(meal.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void editNotExistingFail() {
        Meal meal = new Meal(NEW_MEAL_ID, LocalDateTime.of(2022, 10, 3, 15, 21), "la meal", 507, USER_ID);
        controller.save(meal);
    }
}