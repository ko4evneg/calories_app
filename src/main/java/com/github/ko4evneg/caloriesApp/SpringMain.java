package com.github.ko4evneg.caloriesApp;

import com.github.ko4evneg.caloriesApp.web.meal.MealController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            MealController mealController = appCtx.getBean(MealController.class);

            System.out.println(mealController.getBetween(
                    LocalDate.of(2022, Month.APRIL, 29),
                    null,
                    LocalDate.of(2022, Month.APRIL, 29),
                    null));
        }
    }
}
