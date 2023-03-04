package com.marketplace.logger.common;

public interface ApplicationConstants {

    String TEST_HOST = "localhost";
    int TEST_PORT = 4567;
    String CLASSPATH = "classpath:";

    /*
    extra header
    */
    String FILE = "file";
    String FIELD = "field";
    String EXTRA_HEADER = "extra";

    /*
    Request bodies
    */
    String REQUEST_BODY_1 = "requests/requestBody_1.json";
    String REQUEST_BODY_2 = "requests/requestBody_2.json";
    String REQUEST_BODY_3 = "requests/requestBody_3.json";
    String REQUEST_BODY_4 = "requests/requestBody_4.json";
    String REQUEST_BODY_5 = "requests/requestBody_5.json";

    /*
    Request parts
    */
    String REQUEST_PART_1 = "inputFiles/test-text.txt";
    String REQUEST_PART_2 = "inputFiles/test-image.jpg";

    /*
    Called endpoints
    */
    String API_PREFIX = "/api/v1";
    String ENDPOINT_1 = "/logger-aspect-1/{var}";
    String ENDPOINT_2 = "/logger-aspect-2/{var}";
    String ENDPOINT_3 = "/logger-aspect-3/{var}";
    String ENDPOINT_4 = "/logger-aspect-4/{var}";
    String ENDPOINT_5 = "/logger-aspect-5/{var}";
    String ENDPOINT_6 = "/logger-aspect-6/{arg1}/{arg2}";
    String ENDPOINT_7 = "/logger-aspect-7";
    String ENDPOINT_8 = "/logger-aspect-8/{arg1}";
    String ENDPOINT_9 = "/logger-aspect-9/{arg1}";
    String ENDPOINT_10 = "/logger-aspect-10";
    String ENDPOINT_11 = "/logger-aspect-11";
    String ENDPOINT_12 = "/logger-aspect-12";
    String ENDPOINT_13 = "/logger-aspect-13";
    String ENDPOINT_14 = "/logger-aspect-14";
    String ENDPOINT_15 = "/logger-aspect-15";
    String ENDPOINT_16 = "/logger-aspect-16";
    String ENDPOINT_17 = "/logger-aspect-17";
    String ENDPOINT_18 = "/logger-aspect-18";
    String ENDPOINT_19 = "/logger-aspect-19";
    String ENDPOINT_20 = "/logger-aspect-20/{var}";
    String ENDPOINT_21 = "/logger-aspect-21/{var}";
    String ENDPOINT_22 = "/logger-aspect-22";
    String ENDPOINT_23 = "/logger-aspect-23";
    String ENDPOINT_24 = "/logger-aspect-24";
    String ENDPOINT_25 = "/logger-aspect-25";
    String ENDPOINT_26 = "/logger-aspect-26";

    String ENDPOINT_30 = "/logger-aspect-30";
    String ENDPOINT_31 = "/logger-aspect-31";
    String ENDPOINT_32 = "/logger-aspect-32";

    String TEST_ENDPOINT_1 = "/logger-aspect-1/any";
    String TEST_ENDPOINT_2 = "/logger-aspect-2/any?one=123";
    String TEST_ENDPOINT_3 = "/logger-aspect-3/any?one=something";
    String TEST_ENDPOINT_4 = "/logger-aspect-4/123?one=true";
    String TEST_ENDPOINT_5 = "/logger-aspect-5/123";
    String TEST_ENDPOINT_6 = "/logger-aspect-6/123/something-special";
    String TEST_ENDPOINT_7 = "/logger-aspect-7?one=any-string&two=123&three=false";
    String TEST_ENDPOINT_8 = "/logger-aspect-8/any?one=any&two=123&three=true";
    String TEST_ENDPOINT_9 = "/logger-aspect-9/any?one=any&two=123&three=true";
    String TEST_ENDPOINT_10 = "/logger-aspect-10?one=something";
    String TEST_ENDPOINT_11 = "/logger-aspect-11?one=something";
    String TEST_ENDPOINT_12 = "/logger-aspect-12?one=something";
    String TEST_ENDPOINT_13 = "/logger-aspect-13?one=something&three=true";
    String TEST_ENDPOINT_14 = "/logger-aspect-14?two=true";
    String TEST_ENDPOINT_15 = "/logger-aspect-15";
    String TEST_ENDPOINT_16 = "/logger-aspect-16";
    String TEST_ENDPOINT_17 = "/logger-aspect-17?one=any&two=true";
    String TEST_ENDPOINT_18 = "/logger-aspect-18?one=any&two=true";
    String TEST_ENDPOINT_19 = "/logger-aspect-19?one=any&two=true";
    String TEST_ENDPOINT_20 = API_PREFIX.concat("/logger-aspect-20/any-arg");
    String TEST_ENDPOINT_21 = API_PREFIX.concat("/logger-aspect-21/arg?one=something");
    String TEST_ENDPOINT_22 = API_PREFIX.concat("/logger-aspect-22?one=any&two=true");
    String TEST_ENDPOINT_23 = API_PREFIX.concat("/logger-aspect-23?one=any&two=true");
    String TEST_ENDPOINT_24 = API_PREFIX.concat("/logger-aspect-24?one=any&two=true");
    String TEST_ENDPOINT_25 = API_PREFIX.concat("/logger-aspect-25?one=any&two=true");
    String TEST_ENDPOINT_26 = API_PREFIX.concat("/logger-aspect-26");

    String TEST_ENDPOINT_30 = "/logger-aspect-30";
    String TEST_ENDPOINT_31 = "/logger-aspect-31";
    String TEST_ENDPOINT_32 = "/logger-aspect-32";

}
