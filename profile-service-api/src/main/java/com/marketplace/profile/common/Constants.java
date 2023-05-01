package com.marketplace.profile.common;

public interface Constants {
    String ROLE_DELIMITER = " ";
    String PROFILE_EMAIL = "marketplace.sendto@gmail.com";

    /*
    profile paths:
    */
    String COMMON_PATH_PREFIX = "/api/v1/profile";
    String SAVE_PROFILE_PATH = "/new-profile";
    String UPDATE_PROFILE_PATH = "/update-profile";
    String CONFIRM_PROFILE_CHANGES_PATH = "/update-profile/{profileId}/confirm";
    String VERIFY_PROFILE_PATH = "/verify-profile";
    String BLOCK_PROFILE_PATH = "/block-profile/{profileId}";
    String UNBLOCK_PROFILE_PATH = "/unblock-profile/{profileId}";
    String DELETE_PROFILE = "/delete-profile/{profileId}";
    String GET_ALL_UNBLOCKED_PROFILES_PATH = "/get-all-unblocked";
    String GET_ALL_BLOCKED_PROFILES_PATH = "/get-all-blocked";
    String REQUEST_EMAIL_AGAIN = "/request-email-confirmation";

    String PROFILE_CONFIRMATION_MAIL_TOPIC = "Profile confirmation";

    /*
    headers
     */
    String HEADER_PROFILE_ID = "Profile-Id";
    String PROFILE_ID_VAR = "profileId";
    String ROLES = "Roles";

    /*
    Profile entity fields
    */
    String PROFILE_TABLE_NAME = "profiles";
    String ID = "id";
    String USERNAME = "username";
    String PASSWORD = "pass";
    String EMAIL = "email";
    String ROLES_FIELD = "roles";
    String ACTIVE = "active";
    String BLOCKED = "blocked";
    String CREATED_AT = "created_at";
    String UPDATED_AT = "updated_at";
    String EMAIL_SEND_AT = "email_send_at";

    /*
    sort by time in pagination
    */
    String DESC = "DESC";
    String ASC = "ASC";
}
