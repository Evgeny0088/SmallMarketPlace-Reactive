package com.marketplace.logger.aspect.utils;

import com.marketplace.logger.aspect.common.Constants;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ColorUtils implements Constants {

    public static String coloredString(String inputString, String color) {
        return Arrays.stream(inputString.split(NEW_LINE_DELIMITER))
                .toList().stream()
                .map(color::concat).collect(Collectors.joining(NEW_LINE_DELIMITER));
    }
}
