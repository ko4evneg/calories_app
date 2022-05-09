package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.model.User;

import java.util.List;

public interface UserService {

    User get(Integer userId);

    User getByEmail(String email);

    List<User> getAll();

    void delete(int userId);

    User save(User user);
}
