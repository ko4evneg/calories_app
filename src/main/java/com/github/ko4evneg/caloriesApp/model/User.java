package com.github.ko4evneg.caloriesApp.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.github.ko4evneg.caloriesApp.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractNamedEntity {

    private String email;

    private String password;

    private boolean enabled;

    private Date registered = new Date();

    @Setter(AccessLevel.NONE)
    private Set<Role> roles;

    private int caloriesPerDay;


    public User(String name, String email, String password, Role... roles) {
        this(null, name, email, password, DEFAULT_CALORIES_PER_DAY, true, Arrays.asList((roles)));
    }

    public User(Integer id, String name, String email, String password, Role... roles) {
        this(id, name, email, password, DEFAULT_CALORIES_PER_DAY, true, Arrays.asList((roles)));
    }

    public User(String name, String email, String password, int caloriesPerDay, boolean enabled, Role... roles) {
        this(null, name, email, password, DEFAULT_CALORIES_PER_DAY, enabled, Arrays.asList((roles)));
    }

    public User(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }
}