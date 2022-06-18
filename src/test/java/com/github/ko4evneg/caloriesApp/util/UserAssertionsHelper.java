package com.github.ko4evneg.caloriesApp.util;

import com.github.ko4evneg.caloriesApp.model.User;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserAssertionsHelper implements AssertionsHelper<User> {
    @Override
    public void assertRecursiveEquals(User actual, User expected) {
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("registered", "roles")
                .isEqualTo(expected);
    }

    @Override
    public void assertAllRecursiveEquals(Collection<User> actual, Collection<User> expected) {
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("registered", "roles")
                .isEqualTo(expected);
    }
}
