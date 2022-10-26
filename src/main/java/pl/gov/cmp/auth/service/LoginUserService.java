package pl.gov.cmp.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.cmp.auth.configuration.LoginUserConfiguration;
import pl.gov.cmp.auth.exception.UserAccountNotFoundException;
import pl.gov.cmp.auth.model.dto.AuthenticationCommand;
import pl.gov.cmp.auth.model.dto.GenerateTokenDto;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.model.dto.UserWkDto;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;
import pl.gov.cmp.auth.model.mapper.UserAccountMapper;
import pl.gov.cmp.auth.repository.UserAccountRepository;
import pl.gov.cmp.auth.security.model.CmpUserDetails;
import pl.gov.cmp.auth.security.utils.JwtTokenUtils;
import pl.gov.cmp.auth.service.hmac.HMACService;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

import static pl.gov.cmp.auth.model.enums.UserAccountStatusEnum.NEW;

@Service
@AllArgsConstructor
@Slf4j
public class LoginUserService {

    private final LoginUserConfiguration loginUserConfiguration;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;
    private final RestTemplate restTemplate;
    private final JwtTokenUtils jwtTokenUtils;
    private final HMACService hmacService;
    private final PeselValidatorService peselValidatorService;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<String> generateJwtToken(String ackToken) {
        String token = ackToken.replaceFirst("ack=", "").trim();
        Optional<CmpUserDetails> userDetails = getUserDetails(token);
        return userDetails.map(this.jwtTokenUtils::generateToken);
    }

    @Transactional
    public String generateJwtToken(AuthenticationCommand command) {
        final var userLocalId = command.getUserLocalId();
        final var user = userAccountRepository.findByLocalId(userLocalId).orElseThrow(() -> new BadCredentialsException("Bad credentials"));
        validatePassword(user.getLocalPassword(), command.getPassword());
        final var generateTokenDto = new GenerateTokenDto(user.getId(), user.getLocalId(), user.getFirstName(), user.getLastName(), user.getEmail());
        user.setLastLoginAt(Instant.now());
        return jwtTokenUtils.generateToken(generateTokenDto);
    }

    private void validatePassword(String currentEncodedPassword, String requestPassword) {
        if(!passwordEncoder.matches(requestPassword, currentEncodedPassword)) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Transactional
    public Optional<CmpUserDetails> getUserDetails(String wkId, String ackToken) {
        Optional<UserAccountEntity> userAccountEntity = this.userAccountRepository.findByWkId(wkId);
        if (userAccountEntity.isPresent()) {
            Optional<CmpUserDetails> userDetails = getUserDetails(ackToken);
            if (userDetails.isPresent() && userDetails.get().getUser().getWkId().equals(wkId)) {
                return userDetails;
            }
        }
        log.warn("User account not found for wkId: {}", wkId);
        return Optional.empty();
    }

    public Optional<CmpUserDetails> getUserDetails(String ackToken) {
        Optional<UserWkDto> optionalUserWk = getUserDetailsFromWk(ackToken);
        if (optionalUserWk.isEmpty()) {
            return Optional.empty();
        }

        UserWkDto userWk = optionalUserWk.get();
        userWk.setBirthDate(peselValidatorService.getUserBirthDateFromPesel(userWk.getPersonIdentifier()));
        userWk.setPersonIdentifier(hmacService.getHmacHashCode(userWk.getPersonIdentifier()));
        Optional<UserAccountEntity> optionalUserAccountEntity = this.userAccountRepository.findByWkId(userWk.getPersonIdentifier());

        if (optionalUserAccountEntity.isPresent()) {
            UserAccountEntity userAccountEntity = optionalUserAccountEntity.get();
            updateUserAccount(userAccountEntity, userWk);
            UserAccountDto userAccountDto = this.userAccountMapper.toUserAccountDto(userAccountEntity);
            return Optional.of(CmpUserDetails.createWithUserAndToken(userAccountDto, ackToken));
        } else {
            UserAccountEntity userAccountEntity = createUserAccount(userWk);
            log.debug("Create user account for: {} {}", userAccountEntity.getFirstName(), userAccountEntity.getLastName());
            UserAccountDto userAccountDto = this.userAccountMapper.toUserAccountDto(userAccountEntity);
            return Optional.of(CmpUserDetails.createWithUserAndToken(userAccountDto, ackToken));
        }
    }

    public void logout(String token) {
        URI logoutURI = getLogoutURI(token);
        this.restTemplate.delete(logoutURI);
    }

    public UserAccountDto getUserAccount(Long userId) {
        return this.userAccountMapper.toUserAccountDto(getUserAccountById(userId));
    }

    private UserAccountEntity createUserAccount(UserWkDto userWk) {
        UserAccountEntity userAccountEntity = this.userAccountMapper.toUserAccountEntity(userWk);
        final var now = Instant.now();
        userAccountEntity.setFirstLoginAt(now);
        userAccountEntity.setLastLoginAt(now);
        userAccountEntity.setStatus(NEW);
        this.userAccountRepository.save(userAccountEntity);
        return userAccountEntity;
    }

    private void updateUserAccount(UserAccountEntity userAccountEntity, UserWkDto userWk) {
        userAccountEntity.setLastLoginAt(Instant.now());
        userAccountEntity.setBirthDate(userWk.getBirthDate());
        if (userAccountEntity.getFirstName() == null || userAccountEntity.getLastName() == null) {
            userAccountEntity.setFirstName(userWk.getFirstName());
            userAccountEntity.setLastName(userWk.getFamilyName());
        }
        this.userAccountRepository.save(userAccountEntity);
    }

    private Optional<UserWkDto> getUserDetailsFromWk(String token) {
        URI userDetailsURI = getUserDetailsURI(token);
        ResponseEntity<UserWkDto> response = this.restTemplate.getForEntity(userDetailsURI, UserWkDto.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    private URI getLogoutURI(String token) {
        return UriComponentsBuilder
                .fromHttpUrl(loginUserConfiguration.getWkUrl())
                .pathSegment(token, "logout")
                .build().toUri();
    }

    private URI getUserDetailsURI(String token) {
        return UriComponentsBuilder
                .fromHttpUrl(this.loginUserConfiguration.getWkUrl())
                .pathSegment(token)
                .build().toUri();
    }

    private UserAccountEntity getUserAccountById(Long userId) {
        return this.userAccountRepository.findById(userId)
                .orElseThrow(() -> new UserAccountNotFoundException(userId));
    }

}
