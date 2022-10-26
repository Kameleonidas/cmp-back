package pl.gov.cmp.auth.model.dto;

import lombok.Value;

@Value
public class GenerateTokenDto {

    long userId;
    String localId;
    String firstName;
    String lastName;
    String email;
}
