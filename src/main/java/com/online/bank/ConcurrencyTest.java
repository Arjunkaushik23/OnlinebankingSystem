package com.online.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.online.bank.service.AccountService;

@Component
public class ConcurrencyTest implements CommandLineRunner{

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        // Create an account
        var account = accountService.createAccount("John Doe", 1000.0);

        // Simulate concurrent deposits and withdrawals
        Runnable task1 = () -> accountService.deposit(account.getId(), 100.0);
        Runnable task2 = () -> accountService.withdraw(account.getId(), 50.0);

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final balance: " + accountService.getAccount(account.getId()).getBalance());
    }
}
