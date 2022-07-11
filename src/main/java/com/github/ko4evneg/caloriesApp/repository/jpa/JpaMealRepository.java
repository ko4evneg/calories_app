package com.github.ko4evneg.caloriesApp.repository.jpa;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Meal get(Integer id, Integer userId) {
        List<Meal> resultList = entityManager
                .createQuery("select m from Meal m where m.id = :id and m.user.id = :userId", Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getResultList();
        return DataAccessUtils.singleResult(resultList);
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return entityManager
                .createQuery("select m from Meal m where m.user.id = :userId", Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer userId) {
        return entityManager
                .createQuery("select m from Meal m where m.user.id = :userId and m.dateTime >= :startDate and m.dateTime < :endDate", Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDateTime)
                .setParameter("endDate", endDateTime)
                .getResultList();
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            entityManager.persist(meal);
            return meal;
        }

        int resultCount = entityManager.createQuery("""
                update Meal m set
                m.dateTime = :dateTime, m.calories = :calories, m.description = :desc
                where m.id = :id and m.user.id = :userId
                """)
                .setParameter("calories", meal.getCalories())
                .setParameter("desc", meal.getDescription())
                .setParameter("dateTime", meal.getDateTime())
                .setParameter("id", meal.getId())
                .setParameter("userId", meal.getUser().getId())
                .executeUpdate();

        return resultCount == 0 ? null : meal;
    }

    @Override
    public boolean delete(Integer id, Integer userId) {
        int resultCount = entityManager.createNamedQuery("DELETE_MEAL")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate();
        return resultCount != 0;
    }
}
