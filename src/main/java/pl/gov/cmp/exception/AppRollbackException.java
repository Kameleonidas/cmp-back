package pl.gov.cmp.exception;

import lombok.Getter;

@Getter
public class AppRollbackException extends RuntimeException {
    private final int code;
    private final String message;

    public AppRollbackException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public AppRollbackException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
    }
}
