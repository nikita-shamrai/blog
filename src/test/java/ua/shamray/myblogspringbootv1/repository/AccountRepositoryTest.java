package ua.shamray.myblogspringbootv1.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.shamray.myblogspringbootv1.model.Account;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    void itShouldFindAccountByEmail() {
        //given
        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        accountRepository.save(account);
        //when
        Optional<Account> expected = accountRepository.findByEmail("findMe@mail.com");
        //then
        assertThat(expected.orElseThrow()).isEqualTo(account);
    }
}