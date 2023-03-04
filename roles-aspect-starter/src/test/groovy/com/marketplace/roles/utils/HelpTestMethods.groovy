package com.marketplace.roles.utils

import com.marketplace.roles.common.ApplicationConstants
import org.springframework.util.ResourceUtils

class HelpTestMethods implements ApplicationConstants{

    static String readFileAsString(String path) {
        return ResourceUtils.getFile(CLASSPATH.concat(path)).text
    }
}
