package pl.gov.cmp.administration.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class InvitationNotFoundException extends AppRollbackException {

    public InvitationNotFoundException(String requestIdentifier) {
        super(INVITATION_NOT_FOUND, format("Invitation not found [requestIdentifier: %s]", requestIdentifier));
    }

}
