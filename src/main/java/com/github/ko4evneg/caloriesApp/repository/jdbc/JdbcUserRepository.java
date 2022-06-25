package com.github.ko4evneg.caloriesApp.repository.jdbc;

import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);
    private static final BeanPropertyRowMapper<User> USER_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    private void initRepository() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource userProperties = new BeanPropertySqlParameterSource(user);

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
    public User get(Integer id) {
        log.debug("jdbc get user {}", id);
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM USERS WHERE ID = ?", USER_MAPPER, id));
    }

    @Override
    public User getByEmail(String email) {
        log.debug("jdbc get user by email {}", email);
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM USERS WHERE email = ?", USER_MAPPER, email));
    }

    @Override
    public List<User> getAll() {
        log.debug("jdbc getAll users");
        return jdbcTemplate.query("SELECT * FROM USERS", USER_MAPPER);
    }
}
