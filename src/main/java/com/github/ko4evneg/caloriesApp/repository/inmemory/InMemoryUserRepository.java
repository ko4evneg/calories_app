package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final AtomicInteger idCounter = new AtomicInteger(1000001);
    private final Map<Integer, User> users;

    public InMemoryUserRepository() {
        users = new ConcurrentHashMap<>();
    }

    @Override
    public List<User> getAll() {
        log.debug("getAll");
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> get(Integer id) {
        log.debug("get {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        log.debug("getByEmail {}", email);
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public User save(User user) {
        log.debug("save {}", user);
        if (user.isNew()) {
            int newId = idCounter.getAndIncrement();
            user.setId(newId);
            users.put(newId, user);
            return user;
        }
        return users.computeIfPresent(user.getId(),(id, oldUser) ->  user);
    }

    @Override
    public boolean delete(Integer id) {
        log.debug("delete {}", id);
        return users.remove(id) != null;
    }

    //TODO: move to tests
    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository();

        userRepository.save(new User(null, "Alex", "alex@ru.ru", "123", Role.ADMIN));
        userRepository.save(new User(null, "John", "john@ru.ru", "123", Role.USER));
        userRepository.save(new User(null, "Den", "den@ru.ru", "123", Role.USER));
        userRepository.save(new User(null, "Mark", "mark@ru.ru", "123", Role.USER));
        userRepository.save(new User(null, "Lana", "lana@ru.ru", "123", Role.USER));

        System.out.println(userRepository.getAll());

        System.out.println(userRepository.get(1000005));

        System.out.println(userRepository.delete(1000005));
        System.out.println(userRepository.get(1000005));

        userRepository.save(new User(null, "Tester", "test@ru.ru", "555", Role.USER));
        System.out.println(userRepository.get(1000006));
    }
}
