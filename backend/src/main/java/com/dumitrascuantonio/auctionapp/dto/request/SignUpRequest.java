package com.dumitrascuantonio.auctionapp.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Please, enter username.")
    private String username;

    @Email(message = "It should have email format.")
    @NotBlank(message = "Please, enter email.")
    private String email;

    @NotBlank(message = "Please, enter password.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    private String confirmPassword;

}
