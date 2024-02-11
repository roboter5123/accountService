package com.roboter5123.accountservice.rest.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountLogin {

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    private String password;
}
