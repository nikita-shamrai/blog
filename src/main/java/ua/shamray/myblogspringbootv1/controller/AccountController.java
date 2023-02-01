package ua.shamray.myblogspringbootv1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/register")
    public String registerNewAccountTEST(){
        return "TEST SUCCESSFULL";
    }
    //Maybe DTO??
    @PostMapping("/register")
    public ResponseEntity<Account> registerNewAccount(@RequestBody Account account){
        if (accountService.accountExists(account.getEmail())) {
            throw new IllegalArgumentException("Account with email " + account.getEmail() + " already exists.");
        }
        Account savedAccount = accountService.saveNewUser(account);
        return ResponseEntity.ok(savedAccount);
    }

}
