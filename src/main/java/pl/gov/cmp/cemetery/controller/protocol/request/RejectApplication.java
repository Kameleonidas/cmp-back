package pl.gov.cmp.cemetery.controller.protocol.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RejectApplication {
    Long applicationId;
    ReasonsRejectionApplication reasonsRejectionApplication;
    String description;
}
