package com.marketplace.error.handler.common;

public interface Validation {

    String EMAIL_REGEX = "^([\\w][\\.\\'\\_\\-]?){2,192}(?<![\\\\.\\'\\_\\-])(@{1})(?=.{4,63}$)(([\\w][\\-]?){1,})(?<![-])([\\.]{1})([a-zA-Z]{2,})$";
    String USER_NAME_REGEX = "[\\dA-Za-z][\\dA-z\\._]{2,16}";
    String NAME_REGEX = "^([\\p{InARABIC}]{2,58})|([A-Za-zÀ-ÿ][-']?){2,58}|(\\p{InCYRILLIC}[-']?){2,58}|(\\p{InGREEK}[-']?){2,58}$";
    String ABOUT_ME_REGEX = "^(?s).{1,172}$";
    String HASHTAG_REGEX = "#(?!\\d+\\b)[\\p{InCYRILLIC}A-Za-zÀ-ÿ_0-9]*\\b";

    String NON_EMPTY_STRING_REGEX = "(?!^[\\s]+$).+";
    String NOT_A_DIGIT_REGEX = "\\D";

}
