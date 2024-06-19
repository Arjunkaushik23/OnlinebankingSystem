package com.online.bank.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.online.bank.entity.Account;
import com.online.bank.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

     @PostMapping("/create")
     public Account createAccount(@RequestParam String owner, @RequestParam double initialBalance){
        return accountService.createAccount(owner, initialBalance);
     }

     @PostMapping("/deposit")
     public void deposit(@RequestParam Long accountId, @RequestParam double amount){
        accountService.deposit(accountId, amount);
     }

     @PostMapping("/withdraw")
     public void credit(@RequestParam Long accountId, @RequestParam double amount){
        accountService.withdraw(accountId, amount);
     }

}
