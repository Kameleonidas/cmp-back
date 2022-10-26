package pl.gov.cmp.auth.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.gov.cmp.auth.model.enums.UserRole;
import pl.gov.cmp.auth.security.filter.JwtRequestFilter;
import pl.gov.cmp.auth.security.provider.UserAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final UserAuthenticationProvider userAuthenticationProvider;

    private static final String[] ALLOW_GET_PATHS = {
            "/", "/error/**", "/error", "/api/health",
            "/v3/api-docs/**", "/swagger*/**", "/webjars/**",
            "/proxy/api/**", "/web/proxy/**",
            "/local/proxy/api/**", "/local/web/proxy/**",
            "/api/auth/login",
            "/api/auth/local/login",
            "/api/dictionaries/**",
            "/api/cemeteries/**",
            "/api/applications/**",
            "/api/file/**"
    };

    private static final String[] ALLOW_PUT_PATHS = {
            "/api/applications/cemeteries/**"
    };

    private static final String[] ALLOW_POST_PATHS = {
            "/error/**", "/error",
            "/api/auth/login/response",
            "/api/auth/local/login/response",
            "/api/auth/token",
            "/api/auth/token/generate",
            "/api/applications/cemeteries/**",
            "/api/cemeteries/**",
            "/api/file/**"
    };

    private static final String[] SYSTEM_ADMIN_PATHS = {
        // todo
    };

    private static final String[] USER_ADMIN_PATHS = {
        "/api/users/**"
    };

    private static final String[] CEMETERY_ADMIN_PATHS = {
        // todo
    };

    private static final String[] CEMETERY_EMPLOYEE_PATHS = {
        // todo
    };

    private static final String[] APPLICATION_OPERATOR_PATHS = {
        // todo
    };

    private static final String[] CREMATORIUM_EMPLOYEE_PATHS = {
        // todo
    };

    private static final String[] VOIVODESHIP_EMPLOYEE_PATHS = {
        // todo
    };

    private static final String[] IPN_EMPLOYEE_PATHS = {
        // todo
    };

    private static final String[] KPRM_EMPLOYEE_PATHS = {
        // todo
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .headers().frameOptions().sameOrigin().and()
            .headers().cacheControl().disable().and()
            .httpBasic().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
            .authorizeRequests().antMatchers(HttpMethod.GET, ALLOW_GET_PATHS).permitAll().and()
            .authorizeRequests().antMatchers(HttpMethod.PUT, ALLOW_PUT_PATHS).permitAll().and()
            .authorizeRequests().antMatchers(HttpMethod.POST, ALLOW_POST_PATHS).permitAll().and()
            .authorizeRequests().antMatchers(SYSTEM_ADMIN_PATHS).hasAuthority(UserRole.SYSTEM_ADMIN.name()).and()
            .authorizeRequests().antMatchers(USER_ADMIN_PATHS).permitAll().and()
            .authorizeRequests().antMatchers(CEMETERY_ADMIN_PATHS).hasAuthority(UserRole.CEMETERY_ADMIN.name()).and()
            .authorizeRequests().antMatchers(CEMETERY_EMPLOYEE_PATHS).hasAuthority(UserRole.CEMETERY_EMPLOYEE.name()).and()
            .authorizeRequests().antMatchers(APPLICATION_OPERATOR_PATHS).hasAuthority(UserRole.APPLICATION_OPERATOR.name()).and()
            .authorizeRequests().antMatchers(CREMATORIUM_EMPLOYEE_PATHS).hasAuthority(UserRole.CREMATORIUM_EMPLOYEE.name()).and()
            .authorizeRequests().antMatchers(VOIVODESHIP_EMPLOYEE_PATHS).hasAuthority(UserRole.VOIVODESHIP_EMPLOYEE.name()).and()
            .authorizeRequests().antMatchers(IPN_EMPLOYEE_PATHS).hasAuthority(UserRole.IPN_EMPLOYEE.name()).and()
            .authorizeRequests().antMatchers(KPRM_EMPLOYEE_PATHS).hasAuthority(UserRole.KPRM_EMPLOYEE.name()).and()
            .authorizeRequests().anyRequest().hasAuthority(UserRole.SYSTEM_USER.name()).and()
            .cors().disable().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
