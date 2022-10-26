package pl.gov.cmp.auth.security.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.model.enums.UserRole;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
public class CmpUserDetails implements UserDetails {

    private final UserAccountDto user;

    private final String ackToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // todo add correct authorities based on user data
        authorities.add(new SimpleGrantedAuthority(UserRole.SYSTEM_USER.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getWkId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CmpUserDetails createWithUserAndToken(UserAccountDto user, String ackToken) {
        return CmpUserDetails.builder().user(user).ackToken(ackToken).build();
    }
}
