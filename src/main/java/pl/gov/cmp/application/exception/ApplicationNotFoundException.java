package pl.gov.cmp.application.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.APPLICATION_NOT_FOUND;

public class ApplicationNotFoundException extends AppRollbackException {

    public ApplicationNotFoundException(String applicationId) {
        super(APPLICATION_NOT_FOUND, format("Application not found [applicationId: %s]", applicationId));
    }

}
