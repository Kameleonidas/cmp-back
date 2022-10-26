package pl.gov.cmp.auth.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gov.cmp.auth.configuration.LoginUserConfiguration;
import pl.gov.cmp.auth.model.dto.GenerateTokenDto;
import pl.gov.cmp.auth.security.model.CmpUserDetails;
import pl.gov.cmp.auth.model.dto.UserAccountDto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@AllArgsConstructor
public class JwtTokenUtils {

    public static final String CLAIM_ADMIN = "admin";

    private final LoginUserConfiguration loginUserConfiguration;

    public String getSubjectFromJwtToken(String jwtToken) {
        return getClaimFromJwtToken(jwtToken, Claims::getSubject);
    }

    public String getIdFromJwtToken(String jwtToken) {
        return getClaimFromJwtToken(jwtToken, Claims::getId);
    }

    public Date getExpirationDateFromJwtToken(String token) {
        return getClaimFromJwtToken(token, Claims::getExpiration);
    }

    public boolean isAdmin(String jwtToken) {
        final Claims claims = getAllClaimsFromJwtToken(jwtToken);
        return Boolean.parseBoolean(claims.get(CLAIM_ADMIN, String.class));
    }

    public <T> T getClaimFromJwtToken(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromJwtToken(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.loginUserConfiguration.getJwtSecret()).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(CmpUserDetails cmpUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        UserAccountDto user = cmpUserDetails.getUser();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        // todo create needed claims
        claims.put("pesel", user.getWkId());
        return doGenerateToken(claims, user.getWkId(), cmpUserDetails.getAckToken());
    }

    public String generateToken(GenerateTokenDto generateTokenDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", generateTokenDto.getFirstName());
        claims.put("lastName", generateTokenDto.getLastName());
        claims.put("userId", generateTokenDto.getUserId());
        return doGenerateToken(claims, generateTokenDto.getLocalId(), valueOf(generateTokenDto.getUserId()));
    }
    private String doGenerateToken(Map<String, Object> claims, String subject, String id) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setId(id).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.loginUserConfiguration.getJwtTokenValidity()))
                .signWith(SignatureAlgorithm.HS512, this.loginUserConfiguration.getJwtSecret()).compact();
    }

    public boolean isValidToken(String jwtToken) {
        String wkId = getSubjectFromJwtToken(jwtToken);
        String ackToken = getIdFromJwtToken(jwtToken);
        return isNotBlank(wkId) && isNotBlank(ackToken) && isSigned(jwtToken) && isNotExpired(jwtToken);
    }

    private boolean isNotExpired(String jwtToken) {
        Date expiration = getExpirationDateFromJwtToken(jwtToken);
        return new Date().before(expiration);
    }

    public boolean isSigned(String token) {
        return Jwts.parserBuilder().build().isSigned(token);
    }

}
