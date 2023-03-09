package ua.shamray.myblogspringbootv1.integration.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    void findByEmail_shouldFindAccountByEmail() {
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
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(account);
    }

}