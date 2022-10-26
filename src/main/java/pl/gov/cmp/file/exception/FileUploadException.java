package pl.gov.cmp.file.exception;

import lombok.Getter;
import pl.gov.cmp.exception.AppRollbackException;

import static pl.gov.cmp.exception.ErrorCode.*;

@Getter
public class FileUploadException extends AppRollbackException {

    public FileUploadException(String message, Exception e) {
        super(FILE_UPLOAD_ERROR, message, e);
    }

    public FileUploadException(String message) {
        super(FILE_UPLOAD_ERROR, message);
    }

}
