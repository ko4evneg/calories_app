package com.github.ko4evneg.caloriesApp.web.user;

import com.github.ko4evneg.caloriesApp.model.Role;
import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository;
import com.github.ko4evneg.caloriesApp.util.UserAssertionsHelper;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.EnumSet;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.TestingData.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:db/inmemory-db.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("inMemory")
public class InMemoryAdminControllerTest {
    @Autowired
    private AdminController controller;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserAssertionsHelper assertionsHelper;

    @Before
    public void setUp() {
        if (repository instanceof InMemoryUserRepository) {
            ((InMemoryUserRepository) repository).init();
        }
    }

    @Test
    public void deleteExisting() {
        controller.delete(ADMIN_ID);
        assertNull(repository.get(ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        controller.delete(NOT_FOUND_ID);
    }

    @Test
    public void getExisting() {
        User actualAdmin = controller.get(ADMIN_ID);
        assertionsHelper.assertRecursiveEquals(actualAdmin, admin);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        controller.get(NOT_FOUND_ID);
    }

    @Test
    public void getByMailExisting() {
        User actualAdmin = controller.getByMail(admin.getEmail());
        assertionsHelper.assertRecursiveEquals(actualAdmin, admin);
    }

    @Test(expected = NotFoundException.class)
    public void getByMailNotFound() {
        User actualAdmin = controller.getByMail("random@email.com");
        assertionsHelper.assertRecursiveEquals(actualAdmin, admin);
    }


    @Test
    public void getAllFilled() {
        List<User> actualUsers = controller.getAll();
        assertionsHelper.assertAllRecursiveEquals(actualUsers, List.of(admin, user));
    }

    @Test
    public void getAllEmpty() {
        boolean actualAdminDelete = repository.delete(ADMIN_ID);
        boolean actualUserDelete = repository.delete(USER_ID);
        assertTrue(actualAdminDelete);
        assertTrue(actualUserDelete);

        List<User> actualUsers = controller.getAll();
        assertionsHelper.assertAllRecursiveEquals(actualUsers, List.of());
    }

    @Test
    public void createNew() {
        User expectedAdminUser = new User(NEW_USER_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        User adminUser = new User(null, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));

        User createdAdminUser = controller.create(adminUser);
        User actualAdminUser = controller.get(NEW_USER_ID);

        assertionsHelper.assertRecursiveEquals(createdAdminUser, expectedAdminUser);
        assertionsHelper.assertRecursiveEquals(actualAdminUser, expectedAdminUser);
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

        assertionsHelper.assertRecursiveEquals(actualAdminUser, adminUser);
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