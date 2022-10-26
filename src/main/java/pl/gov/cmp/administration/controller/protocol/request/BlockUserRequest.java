package pl.gov.cmp.administration.controller.protocol.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockUserRequest {
    @NotNull
    private Long userId;
}
