package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User get(Integer userId) {
        return userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Not found entity with id " + userId));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new NotFoundException("Not found entity with email " + email));
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void delete(int userId) {
        if (!userRepository.delete(userId)) {
            throw new NotFoundException("Not found entity with id " + userId);
        }
    }

    @Override
    public User save(User user) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.save(user));
        return userOptional
                .orElseThrow(() -> new NotFoundException("Not found entity for update with id " + user.getId()));
    }
}