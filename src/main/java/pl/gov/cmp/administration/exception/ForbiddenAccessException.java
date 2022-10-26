package pl.gov.cmp.administration.exception;

import pl.gov.cmp.exception.AppRollbackException;
import pl.gov.cmp.exception.ErrorCode;

public class ForbiddenAccessException extends AppRollbackException {
    public ForbiddenAccessException() {
        super(ErrorCode.FORBIDDEN_ACCESS, "Access forbidden");
    }
}
