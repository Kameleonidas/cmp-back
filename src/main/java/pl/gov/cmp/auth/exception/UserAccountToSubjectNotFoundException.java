package pl.gov.cmp.auth.exception;

import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class UserAccountToSubjectNotFoundException extends AppRollbackException {

    public UserAccountToSubjectNotFoundException(Long subjectId) {
        super(USER_ACCOUNT_TO_SUBJECT_NOT_FOUND, format("User account to subject not found with id %s", subjectId));
    }

    public UserAccountToSubjectNotFoundException(long subjectId, ObjectCategoryEnum institutionType) {
        super(USER_ACCOUNT_TO_SUBJECT_NOT_FOUND, format("User account to subject with id %s and type %s not found", subjectId, institutionType));
    }

}
