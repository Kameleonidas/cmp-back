package pl.gov.cmp.administration.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class InvitationExpiredException extends AppRollbackException {

    public InvitationExpiredException(String requestIdentifier) {
        super(INVITATION_EXPIRED, format("Invitation expired [requestIdentifier: %s]", requestIdentifier));
    }

}
