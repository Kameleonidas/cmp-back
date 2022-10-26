package pl.gov.cmp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InternalApplicationException extends RuntimeException {

    private final int code;
    private final String message;
}
