package pl.gov.cmp.exception;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ErrorCode {

    public static final int USER_ACCOUNT_NOT_FOUND = 1000;
    public static final int USER_ACCOUNT_TO_SUBJECT_NOT_FOUND = 1002;
    public static final int USER_NOT_FOUND_IN_APPLICATION_CONTEXT= 1003;
    public static final int INVALID_USER_PESEL= 1004;

    public static final int APPLICATION_NOT_FOUND = 1005;
    public static final int CEMETERY_FACILITY_TYPE_NOT_FOUND = 1006;
    public static final int APPLICATION_CEMETERY_NOT_FOUND = 1800;
    public static final int HMAC_EXCEPTION = 5002;

    public static final int CEMETERY_NOT_FOUND = 2000;

    public static final int DICTIONARY_ELEMENT_NOT_FOUND = 3000;

    public static final int FILE_UPLOAD_ERROR = 4000;
    public static final int FILE_DOWNLOAD_ERROR = 4001;
    public static final int FILE_ACCEPT_ERROR = 4002;

    public static final int INVITATION_NOT_FOUND = 5000;
    public static final int INVITATION_EXPIRED = 5001;

    public static final int FORBIDDEN_ACCESS = 6000;

    public static final int INTERNAL_ERROR = 7000;
    public static final int INCONSISTENT_DATA = 7001;
}
