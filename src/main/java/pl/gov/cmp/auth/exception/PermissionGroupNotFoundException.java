package pl.gov.cmp.auth.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.USER_ACCOUNT_NOT_FOUND;

public class PermissionGroupNotFoundException extends AppRollbackException {

    public PermissionGroupNotFoundException(Long permissionGroupId) {
        super(USER_ACCOUNT_NOT_FOUND, format("Permission group not found [permissionGroupId: %s]", permissionGroupId));
    }
}
