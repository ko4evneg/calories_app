package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.util.ServletContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CaloriesServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextHolder.setContext(new ClassPathXmlApplicationContext("spring/spring-app.xml"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextHolder.getContext().close();
    }
}
