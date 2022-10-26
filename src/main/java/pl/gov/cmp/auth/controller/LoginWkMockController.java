package pl.gov.cmp.auth.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.auth.configuration.LoginUserConfiguration;
import pl.gov.cmp.auth.model.dto.UserWkDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Hidden
@Slf4j
@Profile({"local", "DEV"})
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginWkMockController {

    private static final String RESPONSE_ENCODING = ";charset=UTF-8";
    public static final String SYSTEM_ID = "cmp";

    private final SessionService sessionService;
    private final LoginUserConfiguration loginUserConfiguration;

    @GetMapping(value = "/proxy/api/v1/login/wk/user/{systemId}/{token}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<UserWkDto> getUserWk(@PathVariable String systemId, @PathVariable String token) {
        log.info("Get user data from WK");
        UserDto userDto = this.sessionService.getUserFromCache(token);
        return ResponseEntity.ok(userDto.getUserWkDto());
    }

    @DeleteMapping(value = "/proxy/api/v1/login/wk/user/{systemId}/{token}/logout", produces = MediaType.TEXT_HTML_VALUE + RESPONSE_ENCODING)
    public ResponseEntity<Void> logout(@PathVariable String systemId, @PathVariable String token) {
        log.info("Logout from WK");
        this.sessionService.deleteUserFromCache(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/web/proxy/wk/login/{systemId}", produces = MediaType.TEXT_HTML_VALUE + RESPONSE_ENCODING)
    public ResponseEntity<String> login(@PathVariable String systemId) {
        log.info("Login by WK");
        String uuid = UUID.randomUUID().toString();
        this.sessionService.addUserToCache(uuid, createUserWk(), SYSTEM_ID);
        return ResponseEntity.ok(htmlForRedirectToClient(uuid, this.loginUserConfiguration.getReturnBackUrl()));
    }

    @GetMapping(value = "/web/proxy/wk/login/{systemId}/{pesel}", produces = MediaType.TEXT_HTML_VALUE + RESPONSE_ENCODING)
    public ResponseEntity<String> loginPesel(@PathVariable String systemId, @PathVariable String pesel) {
        log.info("Login pesel by WK");
        String uuid = UUID.randomUUID().toString();
        this.sessionService.addUserToCache(uuid, createUserWk(pesel), SYSTEM_ID);
        return ResponseEntity.ok(htmlForRedirectToClient(uuid, this.loginUserConfiguration.getReturnBackUrl()));
    }

    private String htmlForRedirectToClient(String ack, String direction) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body onload='document.forms[0].submit()'>\n" +
                "<form enctype='text/plain' method='post' action='" + direction + "'>\n" +
                "<input type='hidden' name='ack' value='" + ack + "'/>\n" +
                "<noscript>\n" +
                "<p>JavaScript jest wyłączony. Rekomendujemy włączenie. Aby kontynuować, proszę nacisnąć przycisk poniżej.</p>\n" +
                "<input type='submit' value='Kontynuuj'/>\n" +
                "</noscript>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }


    private UserWkDto createUserWk() {
        return UserWkDto.builder()
                .personIdentifier("88051664174")
                .firstName("Jan")
                .familyName("Testowy")
                .ackToken("88051664174")
                .sessionIndex("test")
                .nameId("test")
                .authnInstant(new Date())
                .build();
    }

    private UserWkDto createUserWk(String pesel) {
        UserWkDto userWkDto = createUserWk();
        userWkDto.setPersonIdentifier(pesel);
        userWkDto.setAckToken(pesel);
        return userWkDto;
    }

}

@Data
@AllArgsConstructor
class UserDto {

    private String sessionId;
    private List<String> systemAccess;
    private String userIp;
    private UserWkDto userWkDto;

}

@Service
class SessionService {

    private final LoadingCache<String, UserDto> cacheUser;

    public SessionService() {
        cacheUser = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.DAYS).build(
            new CacheLoader<>() {
                @Override
                public UserDto load(String key) {
                    throw new IllegalStateException();
                }
            }
        );
    }

    public void addUserToCache(String token, UserWkDto userWkDto, String systemId) {
        UserDto userDTO = new UserDto(token, new ArrayList<>(Collections.singletonList(systemId)), "", userWkDto);
        cacheUser.put(token, userDTO);
    }

    public void deleteUserFromCache(String token) {
        cacheUser.invalidate(token);
    }

    public UserDto getUserFromCache(String token) {
        return cacheUser.getIfPresent(token);
    }

}
