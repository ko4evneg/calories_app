package com.github.ko4evneg.caloriesApp.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServletContextHolder {
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private static ClassPathXmlApplicationContext context = null;
}
