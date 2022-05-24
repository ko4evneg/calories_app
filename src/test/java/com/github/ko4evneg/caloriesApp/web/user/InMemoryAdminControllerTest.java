package com.github.ko4evneg.caloriesApp.web.user;

import com.github.ko4evneg.caloriesApp.model.Role;
import com.github.ko4evneg.caloriesApp.model.User;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.ko4evneg.caloriesApp.repository.inmemory.InMemoryUserRepository;
import com.github.ko4evneg.caloriesApp.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static com.github.ko4evneg.caloriesApp.UserTestData.*;
import static org.junit.Assert.*;

public class InMemoryAdminControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InMemoryAdminControllerTest.class);

    private static ConfigurableApplicationContext appCtx;
    private static AdminRestController controller;
    private static InMemoryUserRepository repository;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        log.info("\n{}\n", Arrays.toString(appCtx.getBeanDefinitionNames()));
        controller = appCtx.getBean(AdminRestController.class);
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
        User expectedAdminUser = new User(NEW_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        User adminUser = new User(null, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));

        User createdAdminUser = controller.create(adminUser);
        User actualAdminUser = controller.get(NEW_ID);

        assertEquals(expectedAdminUser, createdAdminUser);
        assertEquals(expectedAdminUser, actualAdminUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewFail() {
        User adminUser = new User(NEW_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
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
        controller.update(adminUser, NEW_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        User adminUser = new User(NEW_ID, "admino", "e@ma.il", "pwd123", 1750, true, EnumSet.of(Role.ADMIN));
        controller.update(adminUser, NEW_ID);
    }
}