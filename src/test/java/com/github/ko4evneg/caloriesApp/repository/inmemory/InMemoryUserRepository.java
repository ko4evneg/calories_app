package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.model.Role;
import org.springframework.stereotype.Repository;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.github.ko4evneg.caloriesApp.TestingData.*;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    public InMemoryUserRepository() {
        //Data for manual testing
        save(new User("User", "user@mail.ru", "123", Role.USER));
        save(new User("User", "user@mail.ru", "123", Role.ADMIN));
    }

    @Override
    public void init() {
        super.init();
        repository.put(1, user);
        repository.put(2, admin);
        idCounter.set(3);
    }

    @Override
    public List<User> getAll() {
        log.debug("getAll");
        return repository.values()
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .toList();
    }

    @Override
    public Optional<User> get(Integer id) {
        log.debug("get {}", id);
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        log.debug("getByEmail {}", email);
        return repository.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            log.debug("save new {}", user);
            int newId = idCounter.getAndIncrement();
            user.setId(newId);
            repository.put(newId, user);
            return user;
        }
        log.debug("edit {}", user);
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(Integer id) {
        log.debug("delete {}", id);
        return repository.remove(id) != null;
    }
}
