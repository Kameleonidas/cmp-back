package pl.gov.cmp.auth.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class UserTokenNotFoundException extends AppRollbackException {

    public UserTokenNotFoundException(String token) {
        super(USER_ACCOUNT_NOT_FOUND, format("User token not found [token: %s]", token));
    }

}
