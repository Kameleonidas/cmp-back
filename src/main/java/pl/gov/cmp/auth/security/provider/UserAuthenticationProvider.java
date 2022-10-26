package pl.gov.cmp.auth.security.provider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.gov.cmp.auth.security.model.UserAuthentication;

@Component
@AllArgsConstructor
@Slf4j
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UserAuthentication) {
            return authentication;
        }
        throw new BadCredentialsException("User authentication failed");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserAuthentication.class.equals(authentication);
    }
}
