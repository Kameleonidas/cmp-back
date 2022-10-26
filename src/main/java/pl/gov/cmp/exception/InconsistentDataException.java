package pl.gov.cmp.exception;

import static java.lang.String.*;
import static pl.gov.cmp.exception.ErrorCode.INCONSISTENT_DATA;

public class InconsistentDataException extends InternalApplicationException {
    public InconsistentDataException(String message) {
        super(INCONSISTENT_DATA, format("Inconsistent application data: %s", message));
    }
}
