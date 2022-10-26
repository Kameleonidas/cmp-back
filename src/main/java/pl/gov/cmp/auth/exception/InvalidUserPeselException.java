package pl.gov.cmp.auth.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.INVALID_USER_PESEL;

public class InvalidUserPeselException extends AppRollbackException {

    public InvalidUserPeselException(String pesel) {
        super(INVALID_USER_PESEL, format("Invalid User Pesel: %s", pesel));
    }
}
