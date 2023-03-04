package com.marketplace.profile.utils;

import com.marketplace.profile.dto.PageableRequest;
import com.marketplace.profile.dto.PageableResponse;
import com.marketplace.profile.dto.ResponseDto;
import com.marketplace.profile.entity.Profile;
import com.marketplace.profile.properties.PageableProperties;
import com.marketplace.profile.properties.ProfileUpdateProperties;
import com.marketplace.profile.repository.ProfileRepo;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.marketplace.profile.common.Constants.ASC;
import static com.marketplace.profile.common.Constants.DESC;

public class RepositoryUtils {

    public static void setupPageable(PageableRequest request, ProfileUpdateProperties profileUpdateProperties) {
        PageableProperties pageableProperties = profileUpdateProperties.getPageableProperties();
        Integer requestPage = request.getPage();
        Integer requestSize = request.getSize();
        int page = requestPage == null || requestPage < 0 ? 0 : requestPage;
        int size =  requestSize == null || requestSize <= 0 || requestSize > pageableProperties.getMaxSizePerPage()
                ? pageableProperties.getDefaultSize()
                : requestSize;
        request.setPage(page * size);
        request.setSize(size);
    }

    public static Mono<ResponseDto<PageableResponse>> getAllProfiles(PageableRequest pageableRequest,
                                                                     ProfileUpdateProperties profileUpdateProps,
                                                                     String profileProperty,
                                                                     ProfileRepo profileRepo,
                                                                     boolean blocked,
                                                                     String message) {
        setupPageable(pageableRequest, profileUpdateProps);
        return profileRepo.getAllBlockedOrUnblockedProfiles(blocked, profileProperty, pageableRequest.getPage(), pageableRequest.getSize())
                .collectList()
                .map((profileList)-> {
                    if (Objects.equals(profileUpdateProps.getPageableProperties().getSortDirection(), DESC)) profileList.sort(Profile.compareByTimeDesc);
                    if (Objects.equals(profileUpdateProps.getPageableProperties().getSortDirection(), ASC)) profileList.sort(Profile.compareByTimeAsc);
                    PageableResponse pageableResponse = new PageableResponse();
                    pageableResponse.setCurrentPage(pageableRequest.getPage() / pageableRequest.getSize());
                    pageableResponse.setSize(pageableRequest.getSize());
                    pageableResponse.setProfileList(profileList);
                    return ResponseDto.<PageableResponse>builder()
                            .message(message)
                            .content(pageableResponse)
                            .build();
                });
    }
}
