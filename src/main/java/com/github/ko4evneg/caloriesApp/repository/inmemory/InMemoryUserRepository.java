package com.github.ko4evneg.caloriesApp.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    @Override
    public boolean delete(Integer id) {
        log.debug("delete {}", id);
        return true;
    }

    @Override
    public User save(User user) {
        log.debug("save {}", user);
        return user;
    }

    @Override
    public Optional<User> get(Integer id) {
        log.debug("get {}", id);
        return null;
    }

    @Override
    public List<User> getAll() {
        log.debug("getAll");
        return Collections.emptyList();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        log.debug("getByEmail {}", email);
        return null;
    }
}
