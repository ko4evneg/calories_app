package com.github.ko4evneg.caloriesApp.util;

import com.github.ko4evneg.caloriesApp.model.Meal;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MealAssertionsHelper implements AssertionsHelper<Meal> {
    @Override
    public void assertRecursiveEquals(Meal actual, Meal expected) {
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Override
    public void assertAllRecursiveEquals(Collection<Meal> actual, Collection<Meal> expected) {
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
