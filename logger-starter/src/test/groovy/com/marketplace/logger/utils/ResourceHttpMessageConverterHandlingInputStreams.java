package com.marketplace.logger.utils;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;

import java.io.IOException;
import java.io.InputStream;

public class ResourceHttpMessageConverterHandlingInputStreams extends ResourceHttpMessageConverter {

    @Override
    protected Long getContentLength(@NotNull Resource resource, MediaType contentType) throws IOException {
        Long contentLength = super.getContentLength(resource, contentType);
        return contentLength == null || contentLength < 0 ? null : contentLength;
    }

    static class MultipartFileResource extends InputStreamResource {

        private final String filename;

        public MultipartFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }
        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() {
            return -1;
        }
    }
}
