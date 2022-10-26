package pl.gov.cmp.auth.security.filter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.gov.cmp.auth.security.model.UserAuthentication;
import pl.gov.cmp.auth.security.model.CmpUserDetails;
import pl.gov.cmp.auth.security.utils.JwtTokenUtils;
import pl.gov.cmp.auth.service.LoginUserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Component
@AllArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final JwtTokenUtils jwtTokenUtils;
    private final LoginUserService loginUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (isValidAuthorizationHeader(authorizationHeader)) {
            String jwtToken = authorizationHeader.substring(7);
            log.debug("jwtToken: {}", jwtToken);
            try {
                if (jwtTokenUtils.isValidToken(jwtToken)) {
                    String wkId = this.jwtTokenUtils.getSubjectFromJwtToken(jwtToken);
                    String ackToken = this.jwtTokenUtils.getIdFromJwtToken(jwtToken);
                    Optional<CmpUserDetails> userDetails = this.loginUserService.getUserDetails(wkId, ackToken);
                    if (userDetails.isEmpty()) {
                        SecurityContextHolder.clearContext();
                        log.error("Cannot find user, wkId:{}, jwtToken:{}", wkId, jwtToken);
                    } else {
                        UserAuthentication userAuthentication = UserAuthentication.createWithUserDetailsAndToken(userDetails.get(), ackToken);
                        userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
                        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
                        log.debug("User successfully login, wkId:{}, jwtToken:{}", wkId, jwtToken);
                    }
                } else {
                    SecurityContextHolder.clearContext();
                    log.error("Invalid JWT Token");
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                log.info("Authentication error", e);
            }
        } else {
            SecurityContextHolder.clearContext();
            logger.warn("Invalid authorization header");
        }
        chain.doFilter(request, response);
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX);
    }
}

