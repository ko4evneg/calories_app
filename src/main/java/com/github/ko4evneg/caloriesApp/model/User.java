package com.github.ko4evneg.caloriesApp.model;

import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.github.ko4evneg.caloriesApp.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

@Getter
@Setter
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (enabled != user.enabled) return false;
        if (caloriesPerDay != user.caloriesPerDay) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (registered != null ? !registered.equals(user.registered) : user.registered != null) return false;
        return roles != null ? roles.equals(user.roles) : user.roles == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (registered != null ? registered.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        result = 31 * result + caloriesPerDay;
        return result;
    }
}