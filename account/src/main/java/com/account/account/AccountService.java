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
        else if (isPasswordStrongEnough(password) == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password not strong enough: \r\n" + isPasswordStrongEnough(password));
        }
        else {
            accountRepository.save(new Account(username, email, password));
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "Succesfully created an account. You can now login.");
        }
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

    private String isPasswordStrongEnough(String pw){
        String message = "";

        if (pw.length() < 12){
            message += "- Password has to be longer than 12 characters. \r\n";
        }

        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])", pw)) {
            message += "- Password has to contain both lowercase and uppercase letters. \r\n";
        }

        if (!Pattern.matches("^(?=.*\\d)", pw)) {
            message += "- Password has to contain atleast one number. \r\n";
        }

        if (!Pattern.matches("(?=[^A-Za-z0-9])", pw)){
            message += "- Password has to contain atleast one special character, e.g., ! @ * þ $. \r\n";
        }

        if (message == "") {
            return null;
        }
        else {
            return message;
        }
    }

    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no account with id " + id));
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }
}
