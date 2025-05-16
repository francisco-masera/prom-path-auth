package org.dargor.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    private String firstName;

    private String lastName;

    @NotBlank
    private String password;

    @Email
    private String email;

}
