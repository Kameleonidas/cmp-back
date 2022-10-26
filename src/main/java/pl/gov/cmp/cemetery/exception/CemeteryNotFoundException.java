package pl.gov.cmp.cemetery.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class CemeteryNotFoundException extends AppRollbackException {

    public CemeteryNotFoundException(Long cemeteryId) {
        super(CEMETERY_NOT_FOUND, format("Cemetery not found [cemeteryId: %s]", cemeteryId));
    }

}
