package pl.gov.cmp.auth.security.facade;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.gov.cmp.auth.security.exception.SecurityContextException;
import pl.gov.cmp.auth.security.model.CmpUserDetails;

import java.util.Optional;

import static pl.gov.cmp.exception.ErrorCode.*;

@Service
public class AuthenticationFacade {

    public long getAuthenticatedUserId() {
        return getContextUser().getUser().getId();
    }

    private CmpUserDetails getContextUser() {
        return Optional.ofNullable(getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(CmpUserDetails.class::isInstance)
                .map(CmpUserDetails.class::cast)
                .orElseThrow(() -> new SecurityContextException(USER_NOT_FOUND_IN_APPLICATION_CONTEXT, "User not fount in application context"));
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
