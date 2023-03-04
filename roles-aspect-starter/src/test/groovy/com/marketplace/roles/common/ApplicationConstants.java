package com.marketplace.roles.common;

public interface ApplicationConstants {

    String TEST_HOST = "localhost";
    int TEST_PORT = 4567;

    String CLASSPATH = "classpath:";
    String CORRECT_OWNER_ID = "a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";

    /*
    Request bodies
    */
    String REQUEST_BODY_1 = "requestBodies/requestBody_1.json";
    String REQUEST_BODY_2 = "requestBodies/requestBody_2.json";
    String REQUEST_BODY_3 = "requestBodies/requestBody_3.json";
    String REQUEST_BODY_4 = "requestBodies/requestBody_4.json";
    String REQUEST_BODY_5 = "requestBodies/requestBody_5.json";
    String REQUEST_BODY_6 = "requestBodies/requestBody_6.json";
    String REQUEST_BODY_7 = "requestBodies/requestBody_7.json";
    String REQUEST_BODY_8 = "requestBodies/requestBody_8.json";
    String REQUEST_BODY_9 = "requestBodies/requestBody_9.json";
    String REQUEST_BODY_10 = "requestBodies/requestBody_10.json";
    String REQUEST_BODY_11 = "requestBodies/requestBody_11.json";

    /*
    Request parts
    */
    String REQUEST_PART_1 = "inputFiles/test-text.txt";
    String REQUEST_PART_2 = "inputFiles/test-image.jpg";

    /*
    extra header
     */
    String FILE = "file";
    String FIELD = "field";
    String EXTRA_HEADER = "extra";

    /*
    test endpoints
    */
    String ENDPOINT_1 = "/test/endpoint-1/{profileId}";
    String ENDPOINT_2 = "/test/endpoint-2/{profileId}";
    String ENDPOINT_3 = "/test/endpoint-3/{profileId}";
    String ENDPOINT_4 = "/test/endpoint-4/{profileId}";
    String ENDPOINT_5 = "/test/endpoint-5";
    String ENDPOINT_6 = "/test/endpoint-6";
    String ENDPOINT_7 = "/test/endpoint-7/{profileId}/{any}";
    String ENDPOINT_8 = "/test/endpoint-8/{profileId}/{any}";
    String ENDPOINT_9 = "/test/endpoint-9/{profileId}";
    String ENDPOINT_10 = "/test/endpoint-10";
    String ENDPOINT_11 = "/test/endpoint-11/post";
    String ENDPOINT_12 = "/test/endpoint-12/{profileId}";
    String ENDPOINT_13 = "/test/endpoint-13/{profileId}/post";
    String ENDPOINT_14 = "/test/endpoint-14/post";
    String ENDPOINT_15 = "/test/endpoint-15/post";
    String ENDPOINT_16 = "/test/endpoint-16/post";
    String ENDPOINT_17 = "/test/endpoint-17/{profileId}";
    String ENDPOINT_18 = "/test/endpoint-18/post";
    String ENDPOINT_19 = "/test/endpoint-19/post";
    String ENDPOINT_20 = "/test/endpoint-20/get";
    String ENDPOINT_21 = "/test/endpoint-21/get";
    String ENDPOINT_22 = "/test/endpoint-22/get";
    String ENDPOINT_23 = "/test/endpoint-23/{profileId}";
    String ENDPOINT_24 = "/test/endpoint-24/{profileId}";
    String ENDPOINT_25 = "/test/endpoint-25/{profileId}";
    String ENDPOINT_26 = "/test/endpoint-26/{profileId}";
    String ENDPOINT_27 = "/test/endpoint-27/post";
    String ENDPOINT_28 = "/test/endpoint-28/post";
    String ENDPOINT_29 = "/test/endpoint-29/{profileId}";
    String ENDPOINT_30 = "/test/endpoint-30/{profileId}";
    String ENDPOINT_31 = "/test/endpoint-31/{profileId}";
    String ENDPOINT_32 = "/test/endpoint-32/post";
    String ENDPOINT_33 = "/test/endpoint-33/post";
    String ENDPOINT_34 = "/test/endpoint-34/post";
    String ENDPOINT_35 = "/test/endpoint-35/post";
    String ENDPOINT_36 = "/test/endpoint-36/post";
    String ENDPOINT_37 = "/test/endpoint-37/{profileId}";
    String ENDPOINT_38 = "/test/endpoint-38/{profileId}";
    String ENDPOINT_39 = "/test/endpoint-39/{profileId}";
    String ENDPOINT_40 = "/test/endpoint-40/{profileId}";
    String ENDPOINT_41 = "/test/endpoint-41";
    String ENDPOINT_42 = "/test/endpoint-42";
    String ENDPOINT_43 = "/test/endpoint-43/{profileId}/{any}";
    String ENDPOINT_44 = "/test/endpoint-44/{profileId}/{any}";
    String ENDPOINT_45 = "/test/endpoint-45";
    String ENDPOINT_46 = "/test/endpoint-46";
    String ENDPOINT_47 = "/test/endpoint-47/{profileId}";
    String ENDPOINT_48 = "/test/endpoint-48";
    String ENDPOINT_49 = "/test/endpoint-49/{profileId}";
    String ENDPOINT_50 = "/test/endpoint-50/{profileId}/{any}";
    String ENDPOINT_51 = "/test/endpoint-51/{profileId}/{any}";
    String ENDPOINT_52 = "/test/endpoint-52";
    String ENDPOINT_53 = "/test/endpoint-53";

    String TEST_ENDPOINT_1 = "/test/endpoint-1/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_2 = "/test/endpoint-2/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=123";
    String TEST_ENDPOINT_3 = "/test/endpoint-3/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=true";
    String TEST_ENDPOINT_4 = "/test/endpoint-4/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=any&two=23&three=any";
    String TEST_ENDPOINT_5 = "/test/endpoint-5?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b&three=any";
    String TEST_ENDPOINT_6 = "/test/endpoint-6?one=any&two=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_7 = "/test/endpoint-7/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b/any?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b&two=any";
    String TEST_ENDPOINT_8 = "/test/endpoint-8/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=any";
    String TEST_ENDPOINT_9 = "/test/endpoint-9/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_10 = "/test/endpoint-10?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b&three=any";
    String TEST_ENDPOINT_11 = "/test/endpoint-11/post";
    String TEST_ENDPOINT_12 = "/test/endpoint-12/wrong-id123";
    String TEST_ENDPOINT_13 = "/test/endpoint-13/a68dbcd2-2d54-4aa7-a6e4-036525/post";
    String TEST_ENDPOINT_14 = "/test/endpoint-14/post?one=any";
    String TEST_ENDPOINT_15 = "/test/endpoint-15/post";
    String TEST_ENDPOINT_16 = "/test/endpoint-16/post?one=any&two=23&three=any";
    String TEST_ENDPOINT_17 = "/test/endpoint-17/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_18 = "/test/endpoint-18/post?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_19 = "/test/endpoint-19/post";
    String TEST_ENDPOINT_20 = "/test/endpoint-20/get";
    String TEST_ENDPOINT_21 = "/test/endpoint-21/get?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_22 = "/test/endpoint-22/get";
    String TEST_ENDPOINT_23 = "/test/endpoint-23/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=123";
    String TEST_ENDPOINT_24 = "/test/endpoint-24/wrong-id";
    String TEST_ENDPOINT_25 = "/test/endpoint-25/a68dbcd2-2d54-4aa7-a6e4-0365253ae19a?one=wrong&two=123&three=wrong-again";
    String TEST_ENDPOINT_26 = "/test/endpoint-26/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=wrong&two=123&three=wrong-again";
    String TEST_ENDPOINT_27 = "/test/endpoint-27/post?one=any";
    String TEST_ENDPOINT_28 = "/test/endpoint-28/post?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_29 = "/test/endpoint-29/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_30 = "/test/endpoint-30/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_31 = "/test/endpoint-31/a68dbcd2-2d54-4aa7-a6e4-0365253a";
    String TEST_ENDPOINT_32 = "/test/endpoint-32/post";
    String TEST_ENDPOINT_33 = "/test/endpoint-33/post";
    String TEST_ENDPOINT_34 = "/test/endpoint-34/post";
    String TEST_ENDPOINT_35 = "/test/endpoint-35/post";
    String TEST_ENDPOINT_36 = "/test/endpoint-36/post";
    String TEST_ENDPOINT_37 = "/test/endpoint-37/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_38 = "/test/endpoint-38/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=123";
    String TEST_ENDPOINT_39 = "/test/endpoint-39/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b?one=true";
    String TEST_ENDPOINT_40 = "/test/endpoint-40/not-owner?one=any&two=23&three=any";
    String TEST_ENDPOINT_41 = "/test/endpoint-41?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b&three=any";
    String TEST_ENDPOINT_42 = "/test/endpoint-42?one=any&two=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_43 = "/test/endpoint-43/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b/any?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b&two=any";
    String TEST_ENDPOINT_44 = "/test/endpoint-44/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b/any?one=any";
    String TEST_ENDPOINT_45 = "/test/endpoint-45?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_46 = "/test/endpoint-46?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_47 = "/test/endpoint-47/not-owner?one=any&two=23&three=any";
    String TEST_ENDPOINT_48 = "/test/endpoint-48?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b&three=any";
    String TEST_ENDPOINT_49 = "/test/endpoint-49/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_50 = "/test/endpoint-50/not-owner/any?one=one&two=any&three=any";
    String TEST_ENDPOINT_51 = "/test/endpoint-51/not-owner/any?one=any";
    String TEST_ENDPOINT_52 = "/test/endpoint-52?one=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";
    String TEST_ENDPOINT_53 = "/test/endpoint-53?one=any&two=a68dbcd2-2d54-4aa7-a6e4-0365253ae19b";

    /*
    Specified roles combinations
    */
    String ROLES_1 = "USER";
    String ROLES_2 = "USER MANAGER";
    String ROLES_3 = "USER ADMIN";
    String ROLES_4 = "USER ADMIN MANAGER";

    String ROLES_EMPTY = "";
    String ROLES_5 = "USERs WRONG";
    String ROLES_6 = "USER WRONG MANAGER";
    String ROLES_7 = "WRONG";
    String ROLES_8 = "ADMIN WRONG";
    String ROLES_9 = "MANAGER ADMIN WRONG";
    String ROLES_10 = "ADMIN";

    /*
    Required roles combinations
    */
    String USER_ROLE = "USER";
    String MANAGER_ROLE = "MANAGER";
    String ADMIN_ROLE = "ADMIN";
    String WRONG_ROLE = "Wrong";
    String L_CASE_ROLE = "admin";
}
