package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MealMemoryRepository implements MealRepository {
    private final Map<Integer, Meal> meals;

    public MealMemoryRepository() {
        meals = new HashMap<>();

        meals.put(1, new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 10, 0), "Breakfast", 500));
        meals.put(2, new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 13, 0), "Dinner", 1000));
        meals.put(3, new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 20, 0), "Supper", 500));
        meals.put(4, new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 0, 0), "Edge case", 100));
        meals.put(5, new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 1000));
        meals.put(6, new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 500));
        meals.put(7, new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 410));
    }

    @Override
    public List<Meal> getAll() {
        return meals.values().stream().toList();
    }

    @Override
    public Optional<Meal> get(Integer id) {
        return Optional.ofNullable(meals.get(id));
    }

    @Override
    public void save(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(Integer id) {
        meals.remove(id);
    }

    public static void main(String[] args) {
        MealRepository mealRepository = new MealMemoryRepository();

        System.out.println(mealRepository.getAll());

        System.out.println(mealRepository.get(5));

        mealRepository.delete(5);
        System.out.println(mealRepository.get(5));

        mealRepository.save(new Meal(LocalDateTime.now(), "desc", 123123));
        System.out.println(mealRepository.get(8));
    }
}
