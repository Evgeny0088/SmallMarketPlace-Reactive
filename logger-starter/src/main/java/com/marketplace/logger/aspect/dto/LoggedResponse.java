package com.marketplace.logger.aspect.dto;

import com.marketplace.logger.aspect.common.Constants;
import lombok.Data;

@Data
public class LoggedResponse <T> implements Constants {

    private String httpCode = String.valueOf(200);
    private String path = EMPTY;
    private T responseBody;

}
