package com.github.ko4evneg.caloriesApp.repository.jdbc;

import com.github.ko4evneg.caloriesApp.model.Meal;
import com.github.ko4evneg.caloriesApp.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class JdbcMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcMealRepository.class);
    private static final BeanPropertyRowMapper<Meal> MEAL_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    void initRepository() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal get(Integer id, Integer userId) {
        log.debug("jdbcMeal: get {}", id);
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM MEALS WHERE ID = ? AND USER_ID = ?", MEAL_MAPPER, id, userId);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        log.debug("jdbcMeal: getAll by user {}", userId);
        return jdbcTemplate.query("SELECT * FROM MEALS WHERE USER_ID = ?", MEAL_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer userId) {
        log.debug("jdbcMeal: getBetweenHalfOpen by user {}", userId);

        MapSqlParameterSource dates = new MapSqlParameterSource()
                .addValue("startDate", startDateTime)
                .addValue("endDate", endDateTime)
                .addValue("userId", userId);

        return namedJdbcTemplate.query("""
                SELECT * FROM MEALS
                WHERE USER_ID = :userId
                AND DATE_TIME >= :startDate
                AND DATE_TIME < :endDate""", dates, MEAL_MAPPER);
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        MapSqlParameterSource mealProperties = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("dateTime", meal.getDateTime())
                .addValue("userId", meal.getUserId());

        if (meal.isNew()) {
            log.debug("jdbcMeal: save {}", meal);
            int newId = jdbcInsert.executeAndReturnKey(mealProperties).intValue();
            meal.setId(newId);
            return meal;
        }
        log.debug("jdbcMeal: edit {}", meal);
        return namedJdbcTemplate.update("""
                UPDATE MEALS SET description = :description,
                CALORIES = :calories,
                DATE_TIME = :dateTime
                WHERE ID = :id AND USER_ID = :userId""", mealProperties) > 0 ? meal : null;
    }

    @Override
    public boolean delete(Integer id, Integer userId) {
        log.debug("jdbcMeal: delete {}", id);
        return jdbcTemplate.update("DELETE FROM MEALS WHERE ID = ? AND USER_ID = ?", id, userId) > 0;
    }
}
