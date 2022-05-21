package com.github.ko4evneg.caloriesApp.web;

import com.github.ko4evneg.caloriesApp.SpringMain;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CaloriesServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SpringMain.setContext(new ClassPathXmlApplicationContext("spring/spring-app.xml"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SpringMain.getContext().close();
    }
}
