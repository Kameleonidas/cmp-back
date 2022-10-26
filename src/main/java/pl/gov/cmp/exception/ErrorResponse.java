package pl.gov.cmp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String message;
    private UUID uuid;
}
