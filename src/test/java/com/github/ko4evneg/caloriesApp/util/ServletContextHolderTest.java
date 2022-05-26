package com.github.ko4evneg.caloriesApp.util;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class ServletContextHolderTest {
    @Test
    public void saveContext() {
        ClassPathXmlApplicationContext actualContext = ServletContextHolder.getContext();

        assertNull(actualContext);

        ServletContextHolder.setContext(new ClassPathXmlApplicationContext("spring/spring-empty-test.xml"));
        actualContext = ServletContextHolder.getContext();

        assertNotNull(actualContext);
    }
}