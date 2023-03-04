package com.marketplace.authservice.common;

public interface Constants {

    String REGISTRATION_MESSAGE_RESPONSE_200 = """
    You have been registered, confirmation email have been sent on your email box,
    please confirm your identity by following the link in your mail.
    In case if you haven,t receive this mail within 1 minute, please request new email notification.
    """;
    String ACCESS_TOKEN_IS_PROVIDED = "Access token is provided.";

    /*
    auth service paths
     */
    String COMMON_PATH_PREFIX = "/api/v1/auth";
    String REGISTRATION_PATH = "/registration";
    String LOGIN_PATH = "/login";
    String REQUEST_EMAIL_AGAIN = "/request-email-confirmation";
    String CONFIRM_PROFILE_CHANGES_PATH = "/update-profile/{profileId}/confirm";

    /*
    webClient paths:
    */
    String COMMON_PREFIX_PROFILE = "/api/v1/profile";
    String SAVE_PROFILE_PATH = "%s/new-profile";
    String VERIFY_PROFILE_PATH = "%s/verify-profile";
    String REQUEST_EMAIL_AGAIN_PATH = "%s/request-email-confirmation";
    String CONFIRM_PROFILE_CHANGES = "%s/update-profile/%s/confirm";
    String PROFILE_ID_VAR = "profileId";
}
