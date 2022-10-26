package pl.gov.cmp.auth.security.exception;

import pl.gov.cmp.exception.InternalApplicationException;

public class SecurityContextException extends InternalApplicationException {
    public SecurityContextException(int errorCode, String message) {
        super(errorCode, message);
    }
}
