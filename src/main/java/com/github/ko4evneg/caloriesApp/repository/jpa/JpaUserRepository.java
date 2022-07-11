package com.github.ko4evneg.caloriesApp.repository.jpa;

import com.github.ko4evneg.caloriesApp.model.User;
import com.github.ko4evneg.caloriesApp.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User get(Integer id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByEmail(String email) {
        return entityManager.createNamedQuery("FIND_BY_EMAIL", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public List<User> getAll() {
        return entityManager.createNamedQuery("FIND_ALL", User.class)
                .getResultList();
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
        return user;
    }

    @Override
    public boolean delete(Integer id) {
        return entityManager.createNamedQuery("DELETE")
                .setParameter("id", id)
                .executeUpdate() != 0;
    }
}
