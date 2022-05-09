package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMemoryRepository implements MealRepository {
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final Map<Integer, Meal> meals;

    public MealMemoryRepository() {
        meals = new ConcurrentHashMap<>();

        synchronized (idCounter) {
            save(new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 10, 0), "Breakfast", 500));
            save(new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 13, 0), "Dinner", 1000));
            save(new Meal(LocalDateTime.of(2022, Month.APRIL, 29, 20, 0), "Supper", 500));
            save(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 0, 0), "Edge case", 100));
            save(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 10, 0), "Breakfast", 1000));
            save(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 13, 0), "Dinner", 500));
            save(new Meal(LocalDateTime.of(2022, Month.APRIL, 30, 20, 0), "Supper", 410));
        }
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
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            int newId = idCounter.getAndIncrement();
            meal.setId(newId);
            meals.put(newId, meal);
            return get(meal.getId()).get();
        }
            return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer id) {
        return meals.remove(id) != null;
    }

    //TODO: move to tests
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
