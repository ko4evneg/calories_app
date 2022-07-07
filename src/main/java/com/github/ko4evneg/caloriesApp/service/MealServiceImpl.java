package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @Override
    public Meal get(Integer mealId, Integer userId) {
        Meal meal = mealRepository.get(mealId, userId);
        if (meal == null) {
            throw new NotFoundException("Not found entity with id " + mealId);
        }
        User user = userRepository.get(userId);
        meal.setUser(user);
        return meal;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        User user = userRepository.get(userId);
        return mealRepository.getAll(userId)
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .map(meal -> {
                    meal.setUser(user);
                    return meal;
                })
                .toList();
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        User user = userRepository.get(userId);
        return mealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId)
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .map(meal -> {
                    meal.setUser(user);
                    return meal;
                })
                .toList();
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        User user = userRepository.get(userId);
        meal.setUser(user);
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
