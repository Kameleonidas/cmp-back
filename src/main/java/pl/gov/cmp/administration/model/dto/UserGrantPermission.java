package pl.gov.cmp.administration.model.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserGrantPermission {
    private String firstName;
    private String lastName;
}
