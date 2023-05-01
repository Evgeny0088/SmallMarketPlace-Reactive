package com.marketplace.config.starter.utils

import com.marketplace.config.starter.common.ApplicationConstants
import org.springframework.util.ResourceUtils

class HelpTestMethods implements ApplicationConstants{

    static String readFileAsString(String path) {
        return ResourceUtils.getFile(CLASSPATH.concat(path)).text
    }

}
