package com.github.ko4evneg.caloriesApp.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServletContextHolder {
    private static ClassPathXmlApplicationContext context = null;

    public static ClassPathXmlApplicationContext getContext() {
        return context;
    }

    public static void setContext(ClassPathXmlApplicationContext context) {
        ServletContextHolder.context = context;
    }
}
