package com.roboter5123.accountservice.service;
import com.roboter5123.accountservice.rest.model.AccountLogin;

public interface AccountService {

    void createAccount(AccountLogin login);

    void updateVerification(String email, String activationToken);

    String createLogin(AccountLogin login);
}
