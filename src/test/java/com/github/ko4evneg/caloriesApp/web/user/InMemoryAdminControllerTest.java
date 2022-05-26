package com.github.ko4evneg.caloriesApp.web.user;

import com.github.ko4evneg.caloriesApp.model.Role;
import com.github.ko4evneg.caloriesApp.model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.EnumSet;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;
import static org.junit.Assert.*;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class InMemoryAdminControllerTest {
    @Autowired
    private AdminController controller;
    @Autowired
    private InMemoryUserRepository repository;

    @Before
    public void setUp() {
        repository.init();
    }

    @Test
    public void deleteExisting() {
        controller.delete(ADMIN_ID);
        assertTrue(repository.get(ADMIN_ID).isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        controller.delete(NOT_FOUND_ID);
    }

    @Test
    public void getExisting() {
        User actualAdmin = controller.get(ADMIN_ID);
        assertEquals(admin, actualAdmin);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        controller.get(NOT_FOUND_ID);
    }

    @Test
    public void getByMailExisting() {
        User actualAdmin = controller.getByMail(admin.getEmail());
        assertEquals(admin, actualAdmin);
    }

    @Test(expected = NotFoundException.class)
    public void getByMailNotFound() {
        User actualAdmin = controller.getByMail("random@email.com");
        assertEquals(admin, actualAdmin);
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
        User expectedAdminUser = new User(NEW_USER_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        User adminUser = new User(null, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));

        User createdAdminUser = controller.create(adminUser);
        User actualAdminUser = controller.get(NEW_USER_ID);

        assertEquals(expectedAdminUser, createdAdminUser);
        assertEquals(expectedAdminUser, actualAdminUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewFail() {
        User adminUser = new User(NEW_USER_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        controller.create(adminUser);
    }

    @Test
    public void update() {
        User adminUser = new User(ADMIN_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        controller.update(adminUser, ADMIN_ID);

        User actualAdminUser = controller.get(ADMIN_ID);

        assertEquals(adminUser, actualAdminUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNotConsistent() {
        User adminUser = new User(ADMIN_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        controller.update(adminUser, NEW_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        User adminUser = new User(NEW_USER_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        controller.update(adminUser, NEW_USER_ID);
    }
}