package pl.gov.cmp.administration.exception;

import pl.gov.cmp.exception.InternalApplicationException;

import static pl.gov.cmp.exception.ErrorCode.*;

public class PersonalIdentifierGenerationException extends InternalApplicationException {
    public PersonalIdentifierGenerationException() {
        super(INTERNAL_ERROR, "Generating unique personal identifier failed");
    }
}
