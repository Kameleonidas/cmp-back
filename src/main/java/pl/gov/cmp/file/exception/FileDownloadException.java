package pl.gov.cmp.file.exception;

import lombok.Getter;
import pl.gov.cmp.exception.AppRollbackException;

import static pl.gov.cmp.exception.ErrorCode.FILE_DOWNLOAD_ERROR;

@Getter
public class FileDownloadException extends AppRollbackException {


    public FileDownloadException(String message, Exception e) {
        super(FILE_DOWNLOAD_ERROR, message, e);
    }

    public FileDownloadException(String message) {
        super(FILE_DOWNLOAD_ERROR, message);
    }

}
