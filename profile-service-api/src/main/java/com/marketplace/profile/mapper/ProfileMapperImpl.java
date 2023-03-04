package com.marketplace.profile.mapper;

import com.marketplace.ProfileDto;
import com.marketplace.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.marketplace.profile.common.Constants.PROFILE_EMAIL;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileMapperImpl implements ProfileMapper {

    private final PasswordEncoder encoder;

    @Override
    public Profile profileDtoToProfile(ProfileDto profileDto) {
        String email = profileDto.getEmail();
        return Profile.builder()
                .id(profileDto.getId())
                .username(profileDto.getUsername())
                .password(encoder.encode(profileDto.getPassword()))
                .email(email != null && !profileDto.getEmail().equals(PROFILE_EMAIL) ? PROFILE_EMAIL : email)
                .roles(profileDto.getRoles())
                .build();
    }

    @Override
    public ProfileDto profileToProfileDto(Profile profile) {
        return ProfileDto.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .roles(profile.getRoles())
                .isActive(profile.isActive())
                .lastModifiedAt(profile.getUpdatedAt())
                .build();
    }
}
