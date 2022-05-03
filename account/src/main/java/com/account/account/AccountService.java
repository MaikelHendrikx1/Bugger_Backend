package com.account.account;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;    

    public int addNewAccount(String username, String email, String password) {
        if (!Pattern.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given string '" + email + "' is not a valid email.");
        }
        else if (getAccountByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An account with email '" + email + "' already exists.");
        } 
        else {
            accountRepository.save(new Account(username, email, password));
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "Succesfully created an account. You can now login.");
        }
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public Account tryLogin(String email, String password){
        Account account = getAccountByEmail(email);

        if (account == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email '" + email + "' does not exist within our system, did you mean to register?");
        }
        else {
            if (account.isPassCorrect(password)) {
                return account;
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The entered password is incorrect.");
            }
        }
    }
}
