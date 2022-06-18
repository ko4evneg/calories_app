package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    /**
     * @return single result of the operation or <code>null</code>, if none found.
     */
    Meal get(Integer id, Integer userId);

    /**
     * @return list of <code>Meal</code>.
     */
    List<Meal> getAll(Integer userId);

    /**
     * @return list of <code>Meal</code> which happens in time interval provided.
     */
    List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer userId);

    /**
     * Saves new <code>Meal</code> if it has no id, or update existing <code>Meal</code>, if user with same id exists.
     * @return saved or updated <code>Meal</code>. <code>null</code>, if no user found for update.
     */
    Meal save(Meal meal, Integer userId);

    /**
     * @return result of the operation
     */
    boolean delete(Integer id, Integer userId);
}
