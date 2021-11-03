package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    JdbcUserDao jdbcUserDao;

    @GetMapping("users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> get() {
        List<User> users= jdbcUserDao.findAll();
        for(User user: users){
            user.setPassword(null);
        }
        return  users;
    }



}
