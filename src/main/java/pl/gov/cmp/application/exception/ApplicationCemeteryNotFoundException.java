package pl.gov.cmp.application.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class ApplicationCemeteryNotFoundException extends AppRollbackException {

    public ApplicationCemeteryNotFoundException(String appCemeteryId) {
        super(APPLICATION_CEMETERY_NOT_FOUND, format("Application cemetery not found [appCemeteryId: %s]", appCemeteryId));
    }

}
