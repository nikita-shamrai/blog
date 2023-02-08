package ua.shamray.myblogspringbootv1.controller;

import jakarta.validation.Valid;
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
    public ResponseEntity<AccountDTO> registerNewAccount(@Valid @RequestBody AccountDTO accountDTO){
        AccountDTO newUser = accountService.saveNewUser(accountDTO);
        return ResponseEntity.ok(newUser);
    }

}
