package pl.gov.cmp.auth.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class UserAccountNotFoundException extends AppRollbackException {

    public UserAccountNotFoundException(Long userAccountId) {
        super(USER_ACCOUNT_NOT_FOUND, format("User account not found [userAccountId: %s]", userAccountId));
    }

    public UserAccountNotFoundException(String wkId) {
        super(USER_ACCOUNT_NOT_FOUND, format("User account not found [wkId: %s]", wkId));
    }

}
