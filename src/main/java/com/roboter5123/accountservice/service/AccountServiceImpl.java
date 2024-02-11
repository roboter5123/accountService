package com.roboter5123.accountservice.service;
import com.roboter5123.accountservice.repository.AccountRepository;
import com.roboter5123.accountservice.repository.LoginRepository;
import com.roboter5123.accountservice.repository.model.Account;
import com.roboter5123.accountservice.repository.model.Login;
import com.roboter5123.accountservice.rest.model.AccountLogin;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final MailService mailService;
    private final LoginRepository loginRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, MailService mailService,
                              LoginRepository loginRepository) {
        this.accountRepository = accountRepository;
        this.mailService = mailService;
        this.loginRepository = loginRepository;
    }

    @Override
    public void createAccount(AccountLogin login) {

        if (accountRepository.findByEmail(login.getEmail()).isPresent()){
            throw new RuntimeException("Account already exists");
        }

        Account account = new Account();
        account.setEmail(login.getEmail());
        account.setActivated(false);

        try {
            account.setSalt(getSalt());
            account.setPassword(hashPassword(login.getPassword(), account.getSalt()));
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }

        account.setActivationToken(RandomStringUtils.randomAlphanumeric(32));
        try {
            mailService.sendActivationMail(account.getEmail(), account.getActivationToken());
        } catch (MessagingException e) {
            throw new RuntimeException("error sending email", e);
        }
        accountRepository.save(account);
    }

    @Override
    public void updateVerification(String email, String activationToken) {
        Account account = accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);

        if (account.getActivated().equals(true)){
            throw new RuntimeException();
        }
        if (!account.getActivationToken().equals(activationToken)){
            throw new RuntimeException();
        }
        account.setActivated(true);
        account.setActivationToken(null);
        accountRepository.save(account);
    }

    @Override
    public String createLogin(AccountLogin attemptedLogin) {

        Account account = accountRepository.findByEmail(attemptedLogin.getEmail()).orElseThrow(RuntimeException::new);

        String attemptedHashedPassword;
        try {
            attemptedHashedPassword = hashPassword(attemptedLogin.getPassword(), account.getSalt());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing Passowrd", e);
        }

        if (!account.getPassword().equals(attemptedHashedPassword)){
            throw new RuntimeException("Passwords don't match");
        }

        List<Login> loginsToDelete = loginRepository.findByEmailAndCreatedBefore(account.getEmail(), LocalDateTime.now().minusDays(7));
        loginRepository.deleteAll(loginsToDelete);

        Login login = new Login();
        login.setLoginToken(getLoginToken(account));
        login.setAccount(account);
        login.setCreatedOn(LocalDateTime.now());
        return loginRepository.save(login).getLoginToken();
    }

    private String getLoginToken(Account account) {
        SecureRandom random = new SecureRandom();
        byte[] string = new byte[256];
        random.nextBytes(string);
        return new String(string);
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return new String(hash);
    }

    private String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] string = new byte[16];
        random.nextBytes(string);
        return new String(string);
    }
}
