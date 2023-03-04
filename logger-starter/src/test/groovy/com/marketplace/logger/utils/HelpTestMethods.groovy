package com.marketplace.logger.utils

import com.marketplace.logger.common.ApplicationConstants
import com.marketplace.logger.dto.DummyDto
import org.springframework.util.ResourceUtils

class HelpTestMethods implements ApplicationConstants {

    static String readFileAsString(String path) {
        return ResourceUtils.getFile(CLASSPATH.concat(path)).text
    }

    static List<DummyDto> generateDummyDto(int size) {
        List<DummyDto> dummyDtoList = new ArrayList<>()
        for (int i = 0; i < size; i++) {
            DummyDto dummyDto = new DummyDto()
            dummyDto.setUsername(dummyDto.getUsername().concat(" - " + i))
            dummyDtoList.add(dummyDto)
        }
        return dummyDtoList
    }
}
