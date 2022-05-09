package com.github.ko4evneg.caloriesApp.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.service.UserService;

import java.util.List;

import static com.github.ko4evneg.caloriesApp.util.ValidationUtil.assureIdConsistent;
import static com.github.ko4evneg.caloriesApp.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    public List<User> getAll() {
        log.info("getAll");
        return userService.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return userService.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return userService.save(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        userService.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        userService.save(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return userService.getByEmail(email);
    }
}