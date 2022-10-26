package pl.gov.cmp.application.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.APPLICATION_NOT_FOUND;
import static pl.gov.cmp.exception.ErrorCode.CEMETERY_FACILITY_TYPE_NOT_FOUND;

public class CemeteryFacilityTypeNotFoundException extends AppRollbackException {

    public CemeteryFacilityTypeNotFoundException(String cemeteryFacilityTypeId) {
        super(CEMETERY_FACILITY_TYPE_NOT_FOUND, format("Cemetery facility type not found [cemeteryFacilityTypeId: %s]", cemeteryFacilityTypeId));
    }

}
