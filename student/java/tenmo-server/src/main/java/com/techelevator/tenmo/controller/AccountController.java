package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    @Autowired
    JdbcAccountDao jdbcAccountDao;

    @GetMapping("account")
    @ResponseStatus(HttpStatus.OK)
    public Account get(Principal principal) {
        System.out.println(principal.getName());
        return jdbcAccountDao.findByUsername(principal.getName());
    }
}
