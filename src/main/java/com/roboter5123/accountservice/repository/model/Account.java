package com.roboter5123.accountservice.repository.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Account {

    @Id
    @Column(name = "email", nullable = false)
    private String email;
    private String password;
    private String salt;
    private String activationToken;
    private Boolean activated;

    @OneToMany(mappedBy="account")
    private Set<Login> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account that)) return false;

        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(salt, that.salt)) return false;
        if (!Objects.equals(activationToken, that.activationToken))
            return false;
        return Objects.equals(activated, that.activated);
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (activationToken != null ? activationToken.hashCode() : 0);
        result = 31 * result + (activated != null ? activated.hashCode() : 0);
        return result;
    }
}
