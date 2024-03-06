package com.marketplace.sale.service.api.dto;

import com.marketplace.sale.service.api.common.Constants;
import com.marketplace.sale.service.api.enums.PageState;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageableRequest {

    @NotNull
    private Long id;
    private Long size;
    private String order = Constants.DESC;
    private Long prevEvaluatedKey;
    private Long nextEvaluatedKey;
    private PageState pageState = PageState.FIRST_PAGE;

}
