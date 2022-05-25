package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.TestingData;
import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import com.github.ko4evneg.caloriesApp.util.MealsUtil;
import com.github.ko4evneg.caloriesApp.util.Util;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
        //Data for manual testing
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510, ADMIN_ID), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500, ADMIN_ID), ADMIN_ID);
    }

    @Override
    public void init() {
        super.init();
        AtomicInteger id = new AtomicInteger(1);
        TestingData.meals.forEach(m -> repository.put(id.getAndIncrement(), m));
        idCounter.set(10);
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
                throw new NotFoundException("Meal not found!");
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
            throw new NotFoundException("Meal not found!");
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

        Collection<Meal> values = mealRepository.getBetweenHalfOpen(
                getStartSearchDay(LocalDate.of(2022, 4, 30)),
                getEndSearchDay(LocalDate.of(2022, 4, 30)),
                1);
        System.out.println(MealsUtil.getFilteredTos(values, 2000, LocalTime.of(10, 0), LocalTime.of(22, 0)));
    }
}
