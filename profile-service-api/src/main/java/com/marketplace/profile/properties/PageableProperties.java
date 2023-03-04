package com.marketplace.profile.properties;

import lombok.Data;

@Data
public class PageableProperties {

    int maxSizePerPage = 10;
    int defaultSize = 5;
    int defaultPage = 0;
    String sortDirection;

}
