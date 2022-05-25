package com.github.ko4evneg.caloriesApp.web.user;

import com.github.ko4evneg.caloriesApp.model.Role;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InMemoryUserControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserControllerTest.class);

    private static ConfigurableApplicationContext appCtx;
    private static ProfileController controller;
    private static InMemoryUserRepository repository;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        log.info("\n{}\n", Arrays.toString(appCtx.getBeanDefinitionNames()));
        controller = appCtx.getBean(ProfileController.class);
        repository = appCtx.getBean(InMemoryUserRepository.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Before
    public void setUp() {
        repository.init();
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
        User expectedUser = new User(NEW_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
        User user = new User(null, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));

        User createdUser = controller.create(user);
        User actualUser = controller.get(NEW_ID);

        assertEquals(expectedUser, createdUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewFail() {
        User user = new User(NEW_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
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
        controller.update(user, NEW_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        User user = new User(NEW_ID, "uzr", "u@z.r", "pwd123", 1750, true, EnumSet.of(Role.USER));
        controller.update(user, NEW_ID);
    }
}