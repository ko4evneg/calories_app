package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryMealRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository mealRepository = new InMemoryMealRepository();

    @Override
    public List<Meal> getAll(Integer userId) {
        return mealRepository.getAll(userId);
    }

    @Override
    public Meal get(Integer mealId, Integer userId) {
        return mealRepository.get(mealId, userId)
                .orElseThrow(() -> new NotFoundException("Not found entity with id " + mealId));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        Optional<Meal> mealOptional = Optional.ofNullable(mealRepository.save(meal, userId));
        return mealOptional
                .orElseThrow(() -> new NotFoundException("Not found entity for update with id " + meal.getId()));
    }

    @Override
    public void delete(Integer mealId, Integer userId) {
        if (!mealRepository.delete(mealId, userId)) {
            throw new NotFoundException("Not found entity with id " + mealId);
        }
    }
}
