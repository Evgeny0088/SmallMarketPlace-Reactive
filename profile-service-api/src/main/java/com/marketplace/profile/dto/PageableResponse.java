package com.marketplace.profile.dto;

import com.marketplace.profile.entity.Profile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageableResponse {

    int currentPage;
    int size;
    List<Profile> profileList = new ArrayList<>();

}
