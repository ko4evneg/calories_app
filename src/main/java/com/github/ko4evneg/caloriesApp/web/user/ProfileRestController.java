package com.github.ko4evneg.caloriesApp.web.user;

import org.springframework.stereotype.Controller;
import com.github.ko4evneg.caloriesApp.model.User;

import static com.github.ko4evneg.caloriesApp.web.SecurityUtil.authUserId;

@Controller
public class ProfileRestController extends AbstractUserController {

    public User get() {
        return super.get(authUserId());
    }

    public void delete() {
        super.delete(authUserId());
    }

    public void update(User user) {
        super.update(user, authUserId());
    }
}