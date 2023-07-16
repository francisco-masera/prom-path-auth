package org.dargor.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    private String firstName;

    private String lastName;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;

}
