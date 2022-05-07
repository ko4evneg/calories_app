package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealMemoryRepository;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository = new MealMemoryRepository();

    @Override
    public List<Meal> getAll() {
        return mealRepository.getAll();
    }

    @Override
    public Optional<Meal> get(Integer mealId) {
        return mealRepository.get(mealId);
    }

    @Override
    public void save(Meal meal) {
        mealRepository.save(meal);
    }

    @Override
    public void delete(Integer mealId) {
        mealRepository.delete(mealId);
    }

    public static void main(String[] args) {
        MealService mealService = new MealServiceImpl();

        System.out.println(mealService.getAll());

        System.out.println(mealService.get(5));

        mealService.delete(5);
        System.out.println(mealService.get(5));

        mealService.save(new Meal(LocalDateTime.now(), "desc", 123123));
        System.out.println(mealService.get(8));

        mealService.save(new Meal(2, LocalDateTime.now(), "desc", 200000));
        System.out.println(mealService.get(2));
    }
}
