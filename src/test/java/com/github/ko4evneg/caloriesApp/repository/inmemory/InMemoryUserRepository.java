package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.model.Role;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;

import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;


@Repository
@Profile("inMemory")
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {
    public InMemoryUserRepository() {
        //Data for manual testing
        save(new User("User", "user@mail.ru", "123", Role.USER));
        save(new User("User", "user@mail.ru", "123", Role.ADMIN));
    }

    @Override
    public void init() {
        super.init();
        repository.put(USER_ID, user);
        repository.put(ADMIN_ID, admin);
        idCounter.set(ADMIN_ID + 1);
    }

    @Override
    public User get(Integer id) {
        log.debug("inMem: get {}", id);
        return repository.get(id);
    }

    @Override
    public User getByEmail(String email) {
        log.debug("inMem: getByEmail {}", email);
        return repository.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        log.debug("inMem: getAll");
        return repository.values()
                .stream()
                .toList();
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            log.debug("inMem: save new {}", user);
            int newId = idCounter.getAndIncrement();
            user.setId(newId);
            repository.put(newId, user);
            return user;
        }
        log.debug("inMem: edit {}", user);
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(Integer id) {
        log.debug("inMem: delete {}", id);
        return repository.remove(id) != null;
    }
}
