package pl.gov.cmp.auth.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.auth.configuration.LoginUserConfiguration;
import pl.gov.cmp.auth.controller.mapper.LoginUserProtocolMapper;
import pl.gov.cmp.auth.controller.protocol.UserAccountResponse;
import pl.gov.cmp.auth.controller.protocol.UserTokenRequest;
import pl.gov.cmp.auth.controller.protocol.UserTokenResponse;
import pl.gov.cmp.auth.model.dto.AuthenticationCommand;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.security.configuration.CurrentUser;
import pl.gov.cmp.auth.security.model.UserAuthentication;
import pl.gov.cmp.auth.service.LoginUserService;
import pl.gov.cmp.auth.service.UserTokenService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class LoginUserController {

    private final LoginUserService loginUserService;
    private final UserTokenService userTokenService;
    private final LoginUserProtocolMapper loginUserProtocolMapper;
    private final LoginUserConfiguration loginUserConfiguration;

    @Operation(summary = "Get current user", tags = "auth")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<UserAccountResponse> getCurrentUser(@Parameter(hidden = true) @CurrentUser UserAccountDto currentUser) {
        UserAccountDto userAccountDto = this.loginUserService.getUserAccount(currentUser.getId());
        UserAccountResponse response = this.loginUserProtocolMapper.toUserAccountResponse(userAccountDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user token", tags = "auth")
    @PostMapping("/token")
    public ResponseEntity<UserTokenResponse> getUserToken(@RequestBody UserTokenRequest request) {
        String jwtToken = this.userTokenService.getTokenFromCache(request.getAuthToken());
        return ResponseEntity.ok(UserTokenResponse.createWithJwtToken(jwtToken));
    }

    @PostMapping("/token/generate")
    public void token(@Valid @RequestBody AuthenticationCommand command, HttpServletResponse httpResponse) throws IOException {
        final var jwtToken = loginUserService.generateJwtToken(command);
        final var authToken = userTokenService.addTokenToCache(jwtToken);
        httpResponse.sendRedirect(this.loginUserConfiguration.getReturnFrontUrl() + "?token=" + authToken);
    }

    @Operation(summary = "Redirect to WK login page", tags = "auth")
    @GetMapping("/login")
    public void login(HttpServletResponse httpResponse) throws IOException {
        httpResponse.sendRedirect(this.loginUserConfiguration.getWkWebUrl());
    }

    @Hidden
    @PostMapping("/login/response")
    @ResponseBody
    public void responseFromWK(@RequestBody String ackToken, HttpServletResponse httpResponse) throws IOException {
        Optional<String> jwtToken = this.loginUserService.generateJwtToken(ackToken);
        if (jwtToken.isPresent()) {
            String authToken = this.userTokenService.addTokenToCache(jwtToken.get());
            httpResponse.sendRedirect(this.loginUserConfiguration.getReturnFrontUrl() + "?token=" + authToken);
        } else {
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            throw new IllegalStateException();
        }
    }

    @Operation(summary = "Logout user", tags = "auth")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@Parameter(hidden = true) @CurrentUser UserAccountDto currentUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser != null && authentication != null) {
            UserAuthentication userAuthentication = (UserAuthentication) authentication;
            this.loginUserService.logout(userAuthentication.getAckToken());
        }
        return ResponseEntity.ok().build();
    }

}
