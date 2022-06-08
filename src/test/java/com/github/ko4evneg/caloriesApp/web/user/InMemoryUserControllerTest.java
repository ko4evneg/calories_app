package com.github.ko4evneg.caloriesApp.web.user;

import com.github.ko4evneg.caloriesApp.model.Role;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.EnumSet;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class InMemoryUserControllerTest {
    @Autowired
    private ProfileController controller;
    @Autowired
    @Qualifier("inMemoryUserRepository")
    private UserRepository repository;

    @Before
    public void setUp() {
        if (repository instanceof InMemoryUserRepository) {
            ((InMemoryUserRepository) repository).init();
        }
    }

    @Test
    public void deleteExisting() {
        controller.delete(USER_ID);
        assertTrue(repository.get(USER_ID).isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        controller.delete(NOT_FOUND_ID);
    }

    @Test
    public void getExisting() {
        User actualUser = controller.get(USER_ID);
        assertEquals(user, actualUser);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        controller.get(NOT_FOUND_ID);
    }

    @Test
    public void getByMailExisting() {
        User actualUser = controller.getByMail(user.getEmail());
        assertEquals(user, actualUser);
    }

    @Test(expected = NotFoundException.class)
    public void getByMailNotFound() {
        User actualUser = controller.getByMail("random@email.com");
        assertEquals(user, actualUser);
    }


    @Test
    public void getAllFilled() {
        List<User> actualUsers = controller.getAll();
        assertEquals(List.of(admin, user), actualUsers);
    }

    @Test
    public void getAllEmpty() {
        boolean actualAdminDelete = repository.delete(ADMIN_ID);
        boolean actualUserDelete = repository.delete(USER_ID);
        assertTrue(actualAdminDelete);
        assertTrue(actualUserDelete);

        List<User> actualUsers = controller.getAll();
        assertEquals(List.of(), actualUsers);
    }

    @Test
    public void createNew() {
        User expectedUser = new User(NEW_USER_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
        User user = new User(null, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));

        User createdUser = controller.create(user);
        User actualUser = controller.get(NEW_USER_ID);

        assertEquals(expectedUser, createdUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewFail() {
        User user = new User(NEW_USER_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
        controller.create(user);
    }

    @Test
    public void update() {
        User user = new User(USER_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
        controller.update(user, USER_ID);

        User actualUser = controller.get(USER_ID);

        assertEquals(user, actualUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNotConsistent() {
        User user = new User(USER_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
        controller.update(user, NEW_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        User user = new User(NEW_USER_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
        controller.update(user, NEW_USER_ID);
    }
}