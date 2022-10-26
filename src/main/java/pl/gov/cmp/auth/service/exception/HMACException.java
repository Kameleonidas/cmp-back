package pl.gov.cmp.auth.service.exception;

import pl.gov.cmp.exception.AppRollbackException;
import pl.gov.cmp.exception.ErrorCode;

import static java.lang.String.format;

public class HMACException extends AppRollbackException {

    private static final  int HMAC_EXCEPTION = ErrorCode.HMAC_EXCEPTION;

    public HMACException() {
        super(HMAC_EXCEPTION, "Error while generate hashcode");
    }
}