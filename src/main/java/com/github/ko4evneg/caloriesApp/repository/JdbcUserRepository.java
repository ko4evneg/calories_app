package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);
    private static final BeanPropertyRowMapper<User> USER_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    private void initRepository(){
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(User user) {
        MapSqlParameterSource userProperties = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            log.debug("jdbc save new user {}", user);

            Number newId = jdbcInsert.executeAndReturnKey(userProperties);
            user.setId(newId.intValue());

            return user;
        } else {
            log.debug("jdbc edit user {}", user);

            int userRowUpdated = namedJdbcTemplate.update("""
                    UPDATE USERS SET
                    name = :name,
                    email = :email,
                    password = :password,
                    registered = :registered,
                    enabled = :enabled,
                    calories_per_day = :caloriesPerDay
                    WHERE id = :id""", userProperties);

            return userRowUpdated == 0 ? null : user;
        }
    }

    @Override
    public boolean delete(Integer id) {
        log.debug("jdbc delete user {}", id);
        return jdbcTemplate.update("DELETE FROM USERS WHERE ID = ?", id) > 0;
    }

    @Override
    public Optional<User> get(Integer id) {
        log.debug("jdbc get user {}", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE ID = ?", USER_MAPPER, id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        log.debug("jdbc get user by email {}", email);
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE email = ?", USER_MAPPER, email));
    }

    @Override
    public List<User> getAll() {
        log.debug("jdbc getAll users");
        return jdbcTemplate.query("SELECT * FROM USERS", USER_MAPPER);
    }
}
