package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryMealRepository;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository = new InMemoryMealRepository();

    @Override
    public List<Meal> getAll() {
        return mealRepository.getAll();
    }

    @Override
    public Meal get(Integer mealId) {
        return mealRepository.get(mealId)
                .orElseThrow(() -> new NotFoundException("Not found entity with id " + mealId));
    }

    @Override
    public Meal save(Meal meal) {
        Optional<Meal> mealOptional = Optional.ofNullable(mealRepository.save(meal));
        return mealOptional
                .orElseThrow(() -> new NotFoundException("Not found entity for update with id " + meal.getId()));
    }

    @Override
    public void delete(Integer mealId) {
        if (!mealRepository.delete(mealId)) {
            throw new NotFoundException("Not found entity with id " + mealId);
        }
    }

    //TODO: move to tests
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
