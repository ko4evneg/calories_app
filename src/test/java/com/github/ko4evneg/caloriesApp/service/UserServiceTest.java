package com.github.ko4evneg.caloriesApp.service;

import com.github.ko4evneg.caloriesApp.TestingData;
import com.github.ko4evneg.caloriesApp.model.Role;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.util.UserAssertionsHelper;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"jpa"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAssertionsHelper assertionsHelper;

    @Test
    public void get() {
        User actualUser = userService.get(USER_ID);
        assertionsHelper.assertRecursiveEquals(actualUser, user);
    }

    @Test
    public void getByEmail() {
        User actualUser = userService.getByEmail(admin.getEmail());
        assertionsHelper.assertRecursiveEquals(actualUser, admin);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateMailCreate() {
        User actualUser = userService.getByEmail(admin.getEmail());
        actualUser.setId(null);
        User duplicateEmailUser = new User(actualUser);
        userService.save(duplicateEmailUser);
    }

    @Test
    public void getAll() {
        List<User> actualUsers = userService.getAll();
        assertionsHelper.assertAllRecursiveEquals(actualUsers, List.of(TestingData.admin, TestingData.user));
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        userService.delete(USER_ID);
        userService.get(USER_ID);
    }

    @Test
    public void save() {
        User expectedUser = new User("usor", "u@s.r", "123", Role.USER);
        userService.save(expectedUser);
        User actualUser = userService.get(USERS_MEAL_ID + 9);
        assertionsHelper.assertRecursiveEquals(actualUser, expectedUser);
    }
}