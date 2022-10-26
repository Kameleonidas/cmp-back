package pl.gov.cmp.auth.security.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    @Getter
    private final String ackToken;

    private UserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String ackToken) {
        super(principal, credentials, authorities);
        this.ackToken = ackToken;
    }

    public static UserAuthentication createWithUserDetailsAndToken(CmpUserDetails cmpUserDetails, String ackToken) {
        return new UserAuthentication(cmpUserDetails, cmpUserDetails.getAuthorities(), cmpUserDetails.getAuthorities(), ackToken);
    }

}
