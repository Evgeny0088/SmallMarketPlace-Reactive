package com.marketplace.profile.utils;

import com.marketplace.ProfileDto;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.mail.sender.service.MailService;
import com.marketplace.profile.common.Constants;
import com.marketplace.profile.common.MessageConstants;
import com.marketplace.profile.entity.Profile;
import com.marketplace.profile.entity.Roles;
import com.marketplace.profile.properties.ProfileUpdateProperties;
import com.marketplace.profile.repository.ProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class HelpServiceMethods implements Constants, MessageConstants {

    public static void roleSetup(Profile profile, Set<String> verifiedRoles) {
        profile.setRoles(verifiedRoles.isEmpty()
                ? Roles.USER.name()
                : !verifiedRoles.contains(Roles.USER.name())
                    ? Roles.USER.name().concat(ROLE_DELIMITER).concat(String.join(ROLE_DELIMITER, verifiedRoles))
                    : String.join(ROLE_DELIMITER, verifiedRoles));
    }

    public static boolean verifyUpdateTime(LocalDateTime profileTime, long timeOffset) {
        LocalDateTime currentTime = LocalDateTime.now();
        return Duration.between(profileTime, currentTime).toMinutes() > timeOffset;
    }

    public static void profileUpdateSetup(Profile profile,
                                          ProfileDto profileInputs,
                                          Set<String> inputRoles,
                                          long timeOffset,
                                          PasswordEncoder encoder) {
        if (!verifyUpdateTime(profile.getUpdatedAt(), timeOffset)) {
            throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(String.format(PROFILE_UPDATE_ERROR, timeOffset));
        }
        String updatedUsername = profileInputs.getUsername();
        String updatedPassword = profileInputs.getPassword();

        profile.setUsername(updatedUsername == null || updatedUsername.isBlank() ? profile.getUsername() : updatedUsername);
        profile.setPassword(updatedPassword == null || updatedPassword.isBlank() ? profile.getPassword() : encoder.encode(updatedPassword));
        profile.setEmail(profile.getEmail());
        profile.setActive(true);
        roleSetup(profile, inputRoles);
        profile.setUpdatedAt(LocalDateTime.now());
    }

    public static void sendMailNotification(Profile profile,
                                            ProfileRepo profileRepo,
                                            MailService mailService,
                                            String mailBody,
                                            ProfileUpdateProperties profileUpdateProperties,
                                            boolean checkMailTimeSent) {
        try {
            long timeOffset = profileUpdateProperties.getRequestEmailAfter();
            if (checkMailTimeSent && !verifyUpdateTime(profile.getEmailSendAt(), timeOffset)) {
                throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(String.format(MAIL_SENT_TOO_EARLY, timeOffset));
            }
            new Thread(()-> mailService.send(profile.getEmail(), PROFILE_CONFIRMATION_MAIL_TOPIC, mailBody)).start();
            profile.setEmailSendAt(LocalDateTime.now());
            profileRepo.save(profile).subscribe();
        }
        catch (Exception exception) {
            if (exception instanceof CustomException){
                throw exception;
            }
            else {
                log.error(String.format(MAIL_SENT_ERROR_LOG, exception.getMessage()));
            }
        }
    }

    public static Mono<Profile> setBlockProfile(ProfileRepo profileRepo, Profile profile) {
        profile.setActive(false);
        profile.setBlocked(true);
        profile.setUpdatedAt(LocalDateTime.now());
        return profileRepo.save(profile);
    }

    public static Mono<Profile> setUnBlockProfile(ProfileRepo profileRepo, Profile profile) {
        profile.setActive(true);
        profile.setBlocked(false);
        profile.setUpdatedAt(LocalDateTime.now());
        return profileRepo.save(profile);
    }

    public static Mono<Profile> setActive(ProfileRepo profileRepo,
                                          Profile profile,
                                          ProfileUpdateProperties profileUpdateProperties,
                                          boolean activeStatus) {
        long mailExpiredAfter = profileUpdateProperties.getExpiredEmailAfter();
        if (verifyUpdateTime(profile.getEmailSendAt(), mailExpiredAfter)) {
            throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(String.format(MAIL_EXPIRED_ERROR));
        }
        profile.setActive(activeStatus);
        profile.setUpdatedAt(LocalDateTime.now());
        return profileRepo.save(profile);
    }

    public static String uuid(){
        return UUID.randomUUID().toString();
    }
}
