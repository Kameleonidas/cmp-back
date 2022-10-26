package pl.gov.cmp.auth.security.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.security.configuration.CurrentUser;
import pl.gov.cmp.auth.security.model.CmpUserDetails;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDataResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserAccountDto.class.equals(parameter.getParameterType()) && parameter.getParameterAnnotation(CurrentUser.class) != null;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isUserDetailsInstance(authentication) ? ((CmpUserDetails) authentication.getPrincipal()).getUser() : null;
    }

    private boolean isUserDetailsInstance(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof CmpUserDetails;
    }
}
