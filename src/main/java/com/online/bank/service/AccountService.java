package com.online.bank.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.online.bank.entity.Account;
import com.online.bank.repository.AccountRepository;

import jakarta.persistence.OptimisticLockException;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public synchronized void deposit(Long accountId, double amount) {
        boolean success = false;
        int attempts = 0;
        while (!success && attempts < 3) { // Retry up to 3 times
            try {
                Optional<Account> accountOptional = accountRepository.findById(accountId);
                if (accountOptional.isPresent()) {
                    Account account = accountOptional.get();
                    account.setBalance(account.getBalance() + amount);
                    accountRepository.save(account);
                    success = true;
                }
            } catch (OptimisticLockException e) {
                attempts++;
                if (attempts >= 3) {
                    throw new RuntimeException("Failed to update account balance after 3 attempts", e);
                }
                // Log the retry attempt (optional)
                System.out.println("Retrying deposit due to OptimisticLockException, attempt " + attempts);
            }
        }
    }

    @Transactional
    public synchronized void withdraw(Long accountId, double amount) {
        boolean success = false;
        int attempts = 0;
        while (!success && attempts < 3) { // Retry up to 3 times
            try {
                Optional<Account> accountOptional = accountRepository.findById(accountId);
                if (accountOptional.isPresent()) {
                    Account account = accountOptional.get();
                    if (account.getBalance() >= amount) {
                        account.setBalance(account.getBalance() - amount);
                        accountRepository.save(account);
                        success = true;
                    } else {
                        throw new RuntimeException("Insufficient funds");
                    }
                }
            } catch (OptimisticLockException e) {
                attempts++;
                if (attempts >= 3) {
                    throw new RuntimeException("Failed to update account balance after 3 attempts", e);
                }
                // Log the retry attempt (optional)
                System.out.println("Retrying withdraw due to OptimisticLockException, attempt " + attempts);
            }
        }
    }

    public Account createAccount(String owner, double initialBalance) {
        Account account = new Account();
        account.setOwner(owner);
        account.setBalance(initialBalance);
        return accountRepository.save(account);
    }

    public Account getAccount(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        return accountOptional.orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
