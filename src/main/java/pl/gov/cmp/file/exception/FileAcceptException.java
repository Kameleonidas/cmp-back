package pl.gov.cmp.file.exception;

import lombok.Getter;
import pl.gov.cmp.exception.AppRollbackException;

import static pl.gov.cmp.exception.ErrorCode.FILE_ACCEPT_ERROR;

@Getter
public class FileAcceptException extends AppRollbackException {

    public FileAcceptException(String message, Exception e) {
        super(FILE_ACCEPT_ERROR, message, e);
    }

    public FileAcceptException(String message) {
        super(FILE_ACCEPT_ERROR, message);
    }

}
