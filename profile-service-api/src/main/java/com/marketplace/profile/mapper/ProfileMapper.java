package com.marketplace.profile.mapper;

import com.marketplace.ProfileDto;
import com.marketplace.profile.entity.Profile;

public interface ProfileMapper {

    Profile profileDtoToProfile(ProfileDto profileDto);

    ProfileDto profileToProfileDto(Profile profile);

}
