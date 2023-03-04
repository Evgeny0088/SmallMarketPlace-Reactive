package com.marketplace.profile.common;

public interface MessageConstants {

    String PROFILE_UPDATE_MESSAGE = "Profile is just modified!";
    String PROFILE_UPDATES_CONFIRMED = "Profile - %s is modified and active!";
    String PROFILE_IS_BLOCKED = "Profile - %s has just blocked by admin!";
    String PROFILE_IS_UNBLOCKED = "Profile - %s has just unblocked by admin!";
    String PROFILE_PASSWORD_WRONG = "Profile password is wrong";
    String PROFILE_BLOCKED_INFO = "Profile is blocked";
    String PROFILE_NOT_FOUND = "Profile with id ( %s ) is not found.";
    String PROFILE_IS_DELETED = "Profile with id ( %s ) is deleted";
    String PROFILE_IS_NOT_ACTIVE = "Profile is not active";
    String UNBLOCKED_PROFILE_LIST = "List of unblocked profiles found";
    String BLOCKED_PROFILE_LIST = "List of blocked profiles found";
    String USERNAME_ERROR = "Username must be specified!";
    String PASSWORD_ERROR = "Password must be specified!";
    String PROFILE_UPDATE_ERROR = "Profile cannot be updated more often than once per %s minutes, please wait a bit";
    String MAIL_ERROR = "Email should not be empty and correct!";
    String MAIL_SENT_ERROR_LOG = "Mail failed to sent, please check details:\n%s";
    String MAIL_SENT_TOO_EARLY = "Mail cannot be sent more often than once per %s minutes";
    String MAIL_EXPIRED_ERROR = "Mail is expired!";
    String MAIL_MESSAGE = """
            Hello %s, please confirm your profile by following this link below:
            http://%s/auth/api/v1/auth/update-profile/%s/confirm
            """;
    String CONFIRMATION_EMAIL_MESSAGE = "Confirmation email have been sent to your email box";

}
