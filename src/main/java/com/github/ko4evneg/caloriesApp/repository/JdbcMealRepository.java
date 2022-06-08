package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMealRepository implements MealRepository{
    @Override
    public List<Meal> getAll(Integer userId) {
        return null;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer userId) {
        return null;
    }

    @Override
    public Optional<Meal> get(Integer id, Integer userId) {
        return Optional.empty();
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        return null;
    }

    @Override
    public boolean delete(Integer id, Integer userId) {
        return false;
    }
}
