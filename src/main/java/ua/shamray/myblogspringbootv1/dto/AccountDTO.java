package ua.shamray.myblogspringbootv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;

    @NotBlank(message = "firstName is blank")
    private String firstName;

    @NotBlank(message = "lastName is blank")
    private String lastName;

    @NotBlank(message = "email is blank")
    @Email(message = "email is not valid")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
