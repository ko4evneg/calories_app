package com.github.ko4evneg.caloriesApp.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

import static com.github.ko4evneg.caloriesApp.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@NamedQueries({@NamedQuery(name = "FIND_BY_EMAIL", query = "select u from User u where u.email = :email"),
        @NamedQuery(name = "DELETE", query = "delete from User u where u.id = :id"),
        @NamedQuery(name = "FIND_ALL", query = "select u from User u")})
public class User extends AbstractNamedEntity {
    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    @NotBlank
    @Length(min = 3)
    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "registered")
    @CreationTimestamp
    private Date registered;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Column(name = "role")
    private Set<Role> roles;

    @Column(name = "calories_per_day")
    private int caloriesPerDay;

    public User(String name, String email, String password, Role... roles) {
        this(null, name, email, password, DEFAULT_CALORIES_PER_DAY, true, Arrays.asList((roles)));
    }

    public User(Integer id, String name, String email, String password, Role... roles) {
        this(id, name, email, password, DEFAULT_CALORIES_PER_DAY, true, Arrays.asList((roles)));
    }

    public User(String name, String email, String password, int caloriesPerDay, boolean enabled, Role... roles) {
        this(null, name, email, password, DEFAULT_CALORIES_PER_DAY, enabled, Arrays.asList((roles)));
        this.caloriesPerDay = caloriesPerDay;
    }

    public User(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        setRoles(roles);
    }

    public User(User user) {
        super(user.id, user.name);
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.caloriesPerDay = user.getCaloriesPerDay();
        this.enabled = user.isEnabled();
        this.registered = user.getRegistered();
        setRoles(user.roles);
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