package com.pado.idleworld;

import com.pado.idleworld.domain.Account;
import com.pado.idleworld.domain.AccountRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import static com.pado.idleworld.domain.AccountRole.ADMIN;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Account account = createAccount("master@gmail.com", "11111111", ADMIN);
            em.persist(account);
        }

        private Account createAccount(String email, String password, AccountRole role) {
            Account account = new Account();
            account.setEmail(email);
            account.setPassword(password);
            account.setRole(role);
            return account;
        }
    }
}