package com.marketplace;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProfileDto implements MessageConstants {

    private String id;

    @NotBlank(message = USERNAME_ERROR)
    private String username;

    @NotBlank(message = PASSWORD_ERROR)
    private String password;

    @NotBlank(message = EMAIL_ERROR)
    private String email;

    private String roles;
    private boolean isActive;
    private LocalDateTime lastModifiedAt;

}
