package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User get(Integer userId) {
        User user = userRepository.get(userId);
        if (user == null) {
            throw new NotFoundException("Not found entity with id " + userId);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.getByEmail(email);
        if (user == null) {
            throw new NotFoundException("Not found entity with email " + email);
        }
        return user;
    }

    /**
     * @return List of <code>User</code> sorted by name and then by email.
     */
    @Override
    public List<User> getAll() {
        return userRepository.getAll()
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .toList();
    }

    @Override
    public User save(User user) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.save(user));
        return userOptional
                .orElseThrow(() -> new NotFoundException("Not found entity for update with id " + user.getId()));
    }

    @Override
    public void delete(int userId) {
        if (!userRepository.delete(userId)) {
            throw new NotFoundException("Not found entity with id " + userId);
        }
    }
}