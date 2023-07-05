package org.dargor.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    private String firstName;

    private String lastName;

    @NotEmpty
    private String password;

    @JsonProperty("user")
    private String email;

}
