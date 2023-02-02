package ua.shamray.myblogspringbootv1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountDTO> registerNewAccount(@RequestBody AccountDTO accountDTO){
        if (accountService.accountExists(accountDTO.getEmail())) {
            throw new IllegalArgumentException("Account with email " + accountDTO.getEmail() + " already exists.");
        }
        AccountDTO newUser = accountService.saveNewUser(accountDTO);
        return ResponseEntity.ok(newUser);
    }

}
