package com.roboter5123.accountservice.rest;
import com.roboter5123.accountservice.rest.model.AccountLogin;
import com.roboter5123.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/accounts")
public class AccountResource {

    private final AccountService accountService;

    @Autowired
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public void createAccount(@Valid @RequestBody AccountLogin login) {
        log.info("createAccount started");
        accountService.createAccount(login);
        log.info("createAccount finished");
    }

    @PutMapping("/{email}/activated")
    public void updateAccountVerification(@PathVariable String email, @RequestParam(name = "token") String activationToken) {
        log.info("updateAccountVerification started");
        accountService.updateVerification(email, activationToken);
        log.info("updateAccountVerification finished");
    }

    @PostMapping("/logins")
    public String createLogin(@Valid @RequestBody AccountLogin login){
        log.info("createLogin started");
        String loginToken = accountService.createLogin(login);
        log.info("createLogin finished");
        return loginToken;
    }
}
