package ua.shamray.myblogspringbootv1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDTO {

    private Long id;
    @NotNull(message = "firstName is null")
    @NotBlank(message = "firstName is blank")
    @NotEmpty(message = "firstName is empty")
    private String firstName;
    @NotNull(message = "lastName is null")
    @NotBlank(message = "lastName is blank")
    @NotEmpty(message = "lastName is empty")
    private String lastName;

    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
