package ua.shamray.myblogspringbootv1.dto;

import lombok.Data;

@Data
public class AccountDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

}
