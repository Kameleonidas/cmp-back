package pl.gov.cmp.cemetery.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.CEMETERY_NOT_FOUND;

public class MinimumCriteriaNotProvidedException extends AppRollbackException {

    public MinimumCriteriaNotProvidedException() {
        super(CEMETERY_NOT_FOUND, "Minimum criteria not provided");
    }

}
