package com.roboter5123.accountservice.repository.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "login")
public class Login {

    @Id
    @Column(name = "token", nullable = false)
    private String loginToken;
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "account_email", nullable = false)
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login login)) return false;

        if (!Objects.equals(loginToken, login.loginToken)) return false;
        if (!Objects.equals(createdOn, login.createdOn)) return false;
        return Objects.equals(account, login.account);
    }

    @Override
    public int hashCode() {
        int result = loginToken != null ? loginToken.hashCode() : 0;
        result = 31 * result + (createdOn != null ? createdOn.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}