package com.marketplace.gateway.api.jwtConfig

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class SignupData {

    private String profileId;

    private String phoneNumber;

    private String phoneCode;

    private String email;

    private String firstName;

    private String lastName;

    private String gender;

    SignupData of(){
        profileId = "33b37784-255b-4e11-8fff-e3092244cd5c"
        phoneNumber = "292890028"
        phoneCode = "375"
        email = "test@test.com"
        firstName = "test-name"
        lastName = "test-name"
        gender = "male"
        return this
    }
}
