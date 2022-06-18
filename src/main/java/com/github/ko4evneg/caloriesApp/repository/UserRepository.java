package com.github.ko4evneg.caloriesApp.repository;

import com.github.ko4evneg.caloriesApp.model.User;

import java.util.List;

public interface UserRepository {
    /**
     * @return single result of the operation or <code>null</code>, if none found.
     */
    User get(Integer id);

    /**
     * @return single result of the operation or <code>null</code>, if none found.
     */
    User getByEmail(String email);

    /**
     * @return list of <code>User</code>.
     */
    List<User> getAll();

    /**
     * Saves new <code>User</code> if it has no id, or update existing <code>User</code>, if user with same id exists.
     * @return saved or updated <code>User</code>. <code>null</code>, if no user found for update.
     */
    User save(User user);

    /**
     * @return result of the operation
     */
    boolean delete(Integer id);
}