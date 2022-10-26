package pl.gov.cmp.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppNoRollbackException extends RuntimeException {
    private final int code;
    private final String message;
}
