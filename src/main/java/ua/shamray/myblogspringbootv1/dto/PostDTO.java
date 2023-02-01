package ua.shamray.myblogspringbootv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {

    private Long id;
    @JsonProperty("account")
    private AccountDTO accountDTO;
    private String title;
    private String body;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
