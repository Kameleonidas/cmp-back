package pl.gov.cmp.auth.controller.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserTokenResponse {

    private String jwtToken;

    public static UserTokenResponse createWithJwtToken(String jwtToken) {
        return new UserTokenResponse(jwtToken);
    }
}
