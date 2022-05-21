package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import com.github.ko4evneg.caloriesApp.util.Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;

import static com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository.ADMIN_ID;
import static com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository.USER_ID;
import static com.github.ko4evneg.caloriesApp.util.DateTimeUtil.getEndSearchDay;
import static com.github.ko4evneg.caloriesApp.util.DateTimeUtil.getStartSearchDay;
import static com.github.ko4evneg.caloriesApp.util.ValidationUtil.checkUserHasRightsForMeal;

@Repository
public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {

    public InMemoryMealRepository() {
        super();

        MealsUtil.meals.forEach(m -> save(m, USER_ID));
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510, ADMIN_ID), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500, ADMIN_ID), ADMIN_ID);
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        log.debug("getAll");
        return getFiltered(userId, m -> true);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer userId) {
        log.debug("getBetweenHalfOpen");
        return getFiltered(userId, m -> Util.isBetweenHalfOpen(m.getDateTime(), startDateTime, endDateTime));
    }

    private List<Meal> getFiltered(Integer userId, Predicate<Meal> filter) {
        log.debug("getAllFiltered");
        return repository.values().stream()
                .filter(meal -> checkUserHasRightsForMeal(meal, userId))
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .toList();
    }

    @Override
    public Optional<Meal> get(Integer id, Integer userId) {
        log.debug("get {}", id);
        Meal meal = repository.get(id);
        if (meal != null && checkUserHasRightsForMeal(meal, userId))
            return Optional.of(meal);
        return Optional.empty();
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            log.debug("save new {}", meal);
            if (!checkUserHasRightsForMeal(meal, userId))
                throw new RuntimeException("User id mismatch with current user");
            int newId = idCounter.getAndIncrement();
            meal.setId(newId);
            repository.put(newId, meal);
            return meal;
        }
        log.debug("edit {}", meal);
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (checkUserHasRightsForMeal(oldMeal, userId))
                return meal;
            //TODO rethrow in service?
            throw new RuntimeException("User id mismatch with current user");
        });
    }

    @Override
    public boolean delete(Integer id, Integer userId) {
        log.debug("delete {}", id);
        Optional<Meal> meal = get(id, userId);
        if (meal.isPresent() && checkUserHasRightsForMeal(meal.get(), userId))
            return repository.remove(id) != null;
        return false;
    }

    //TODO: move to tests
    public static void main(String[] args) {
        MealRepository mealRepository = new InMemoryMealRepository();
 /*
        System.out.println(mealRepository.getAll(1));

        System.out.println(mealRepository.get(5, 1));

        mealRepository.delete(5, 1);
        System.out.println(mealRepository.get(5, 1));

        mealRepository.save(new Meal(LocalDateTime.now(), "desc", 123123, 1), 1);
        System.out.println(mealRepository.get(8, 1));
  */
 /*       Meal meal = new Meal(LocalDateTime.now(), "desc", 123123, 2);
        Meal dupMeal = new Meal(LocalDateTime.now(), "desc", 123123, 2);

        mealRepository.save(meal, 2);
        System.out.println("All user2 meals (should be 1): " + mealRepository.getAll(2));
        System.out.println("All user2 meals (should be 7): " + mealRepository.getAll(1));

        try {
            mealRepository.save(new Meal(LocalDateTime.now(), "desc", 55, 2), 1);
        } catch (Exception e) {
            System.out.println("failed wrong userId in meal save" + mealRepository.getAll(2));
        }

        System.out.println("Correct userId meal retrieval: " + mealRepository.get(1, 1));
        System.out.println("Wrong userId meal retrieval: " + mealRepository.get(1, 2));

        mealRepository.delete(8, 1);
        System.out.println("Wrong user delete: " + mealRepository.getAll(2));

        mealRepository.delete(8, 2);
        System.out.println("Correct delete: " + mealRepository.getAll(2));*/

        Collection<Meal> values = mealRepository.getBetweenHalfOpen(
                getStartSearchDay(LocalDate.of(2022,4,30)),
                getEndSearchDay(LocalDate.of(2022,4,30)),
                1);
        System.out.println(MealsUtil.getFilteredTos(values, 2000, LocalTime.of(10, 0), LocalTime.of(22, 0)));
    }
}
