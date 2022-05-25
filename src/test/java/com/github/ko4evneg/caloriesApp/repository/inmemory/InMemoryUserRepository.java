package com.github.ko4evneg.caloriesApp.repository.inmemory;

import com.github.ko4evneg.caloriesApp.model.Role;
import org.springframework.stereotype.Repository;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.github.ko4evneg.caloriesApp.UserTestData.*;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    public InMemoryUserRepository() {
        //Init for testing
        save(new User("User", "user@mail.ru", "123", Role.USER));
        save(new User("User", "user@mail.ru", "123", Role.ADMIN));
    }

    @Override
    public void init() {
        super.init();
        repository.put(1, user);
        repository.put(2, admin);
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
