package com.marketplace.gateway.api.util;

public class RegexUtil {

    private static final String SINGLE_ASTERISK_MIDDLE_PLACEHOLDER = "%1M%";
    private static final String MULTIPLE_ASTERISK_MIDDLE_PLACEHOLDER = "%2M%";
    private static final String SINGLE_ASTERISK_TAIL_PLACEHOLDER = "%1T%";
    private static final String MULTIPLE_ASTERISK_TAIL_PLACEHOLDER = "%2T%";

    private static final String SINGLE_ASTERISK_MIDDLE = "/[^/]*/";
    private static final String MULTIPLE_ASTERISK_MIDDLE = "/.*/";
    private static final String SINGLE_ASTERISK_TAIL = "/[^/]*";
    private static final String MULTIPLE_ASTERISK_TAIL = "/.*";

    public static String ruleToRegex(String rule) {
        rule = rule.trim();
        return "^" + rule.trim().replaceAll("(.*?)/$", "$1")
                .replaceAll("/\\*/", SINGLE_ASTERISK_MIDDLE_PLACEHOLDER)
                .replaceAll("/\\*+/", MULTIPLE_ASTERISK_MIDDLE_PLACEHOLDER)
                .replaceAll("/\\*$", SINGLE_ASTERISK_TAIL_PLACEHOLDER)
                .replaceAll("/\\*+$", MULTIPLE_ASTERISK_TAIL_PLACEHOLDER)
                .replaceAll(SINGLE_ASTERISK_MIDDLE_PLACEHOLDER, SINGLE_ASTERISK_MIDDLE)
                .replaceAll(MULTIPLE_ASTERISK_MIDDLE_PLACEHOLDER, MULTIPLE_ASTERISK_MIDDLE)
                .replaceAll(SINGLE_ASTERISK_TAIL_PLACEHOLDER, SINGLE_ASTERISK_TAIL)
                .replaceAll(MULTIPLE_ASTERISK_TAIL_PLACEHOLDER, MULTIPLE_ASTERISK_TAIL)+ "/*$";
    }
}
