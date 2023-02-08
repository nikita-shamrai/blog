package ua.shamray.myblogspringbootv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;

    @JsonProperty("account")
    private AccountViewer accountViewer;

    @NotBlank(message = "post title is blank")
    @NotEmpty(message = "post title is empty")
    @Size(max = 50, message = "post title is limit to 50 characters")
    private String title;

    @NotBlank(message = "post body is blank")
    @NotEmpty(message = "post body is empty")
    @Size(max = 10000, message = "post body is too big")
    private String body;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
