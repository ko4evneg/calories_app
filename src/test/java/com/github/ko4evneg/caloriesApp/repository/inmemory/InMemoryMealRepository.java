package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.TestingData;
import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import com.github.ko4evneg.caloriesApp.util.Util;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;

import static com.github.ko4evneg.caloriesApp.TestingData.ADMIN_ID;
import static com.github.ko4evneg.caloriesApp.TestingData.NEW_USER_ID;
import static com.github.ko4evneg.caloriesApp.util.ValidationUtil.checkUserHasRightsForMeal;

@Repository
@Profile("inMemory")
public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {

    public InMemoryMealRepository() {
        super();
        //Data for manual testing
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510, ADMIN_ID), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500, ADMIN_ID), ADMIN_ID);
    }

    @Override
    public void init() {
        super.init();
        idCounter.set(NEW_USER_ID + 1);
        TestingData.meals.forEach(m -> repository.put(idCounter.getAndIncrement(), m));
    }

    @Override
    public Meal get(Integer id, Integer userId) {
        log.debug("inMemMeal: get {}", id);
        Meal meal = repository.get(id);
        if (meal != null && checkUserHasRightsForMeal(meal, userId))
            return meal;
        return null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        log.debug("inMemMeal: getAll");
        return getAllFiltered(userId, m -> true);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer userId) {
        log.debug("inMemMeal: getBetweenHalfOpen");
        return getAllFiltered(userId, m -> Util.isBetweenHalfOpen(m.getDateTime(), startDateTime, endDateTime));
    }

    private List<Meal> getAllFiltered(Integer userId, Predicate<Meal> filter) {
        log.debug("inMemMeal: getAllFiltered");
        return repository.values().stream()
                .filter(meal -> checkUserHasRightsForMeal(meal, userId))
                .filter(filter)
                .toList();
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            log.debug("inMemMeal: save new {}", meal);
            if (!checkUserHasRightsForMeal(meal, userId))
                throw new NotFoundException("Meal not found!");
            int newId = idCounter.getAndIncrement();
            meal.setId(newId);
            repository.put(newId, meal);
            return meal;
        }
        log.debug("inMemMeal: edit {}", meal);
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (checkUserHasRightsForMeal(oldMeal, userId)) {
                return meal;
            } else {
                throw new NotFoundException("Meal not found!");
            }
        });
    }

    @Override
    public boolean delete(Integer id, Integer userId) {
        log.debug("inMemMeal: delete {}", id);
        Meal meal = get(id, userId);
        if (meal != null && checkUserHasRightsForMeal(meal, userId))
            return repository.remove(id) != null;
        return false;
    }
}
