package com.account.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/Account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping(path = "/register")
    public @ResponseBody void addNewAccount(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        accountService.addNewAccount(username, email, password);
    }

    @GetMapping(path = "/login")
    public @ResponseBody Account tryLogin(@RequestParam String email, @RequestParam String password){
        return accountService.tryLogin(email, password);
    }

    @GetMapping(path = "/get")
    public @ResponseBody Account getAccount(@RequestParam Integer id){
        return accountService.getAccountById(id);
    }

    @GetMapping(path = "/getByEmail")
    public @ResponseBody Account getAccount(@RequestParam String email) {
        Account ret = accountService.getAccountByEmail(email);

        if (ret != null){
            return ret;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no account with email " + email);
        }
    }
}
