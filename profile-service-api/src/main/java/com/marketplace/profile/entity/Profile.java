package com.marketplace.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketplace.profile.common.Constants;
import com.marketplace.profile.common.MessageConstants;
import lombok.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

import static com.marketplace.profile.common.Constants.PROFILE_TABLE_NAME;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(PROFILE_TABLE_NAME)
public class Profile implements Constants, MessageConstants {

    @Id
    @Column(ID)
    protected String id;

    @Column(USERNAME)
    @NotBlank(message = USERNAME_ERROR)
    private String username;

    @Column(PASSWORD)
    @NotBlank(message = PASSWORD_ERROR)
    @JsonIgnore
    private String password;

    @Column(EMAIL)
    @Email
    @NotBlank(message = MAIL_ERROR)
    private String email;

    @Column(ROLES_FIELD)
    @Builder.Default
    private String roles = Strings.EMPTY;

    @Column(ACTIVE)
    private boolean isActive;

    @Column(BLOCKED)
    private boolean isBlocked;

    @Column(CREATED_AT)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(UPDATED_AT)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(EMAIL_SEND_AT)
    @Builder.Default
    private LocalDateTime emailSendAt = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Comparator<Profile> compareByTimeDesc = (p1, p2) -> p2.getUpdatedAt().compareTo(p1.getUpdatedAt());
    public static Comparator<Profile> compareByTimeAsc = Comparator.comparing(Profile::getUpdatedAt);
}
