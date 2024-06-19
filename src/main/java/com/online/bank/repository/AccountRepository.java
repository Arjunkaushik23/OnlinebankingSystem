package com.online.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.online.bank.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    
}
