package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    boolean delete(Integer id);

    Optional<User> get(Integer id);

    Optional<User> getByEmail(String email);

    List<User> getAll();
}