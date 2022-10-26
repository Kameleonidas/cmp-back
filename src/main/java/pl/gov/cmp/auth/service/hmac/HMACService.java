package pl.gov.cmp.auth.service.hmac;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.gov.cmp.auth.service.exception.HMACException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static org.locationtech.jts.io.WKBWriter.bytesToHex;

@NoArgsConstructor
@Setter
@AllArgsConstructor
@Slf4j
@Service
public class HMACService {

    @Value("${hmac.key}")
    private String hmacKey;

    private static final String ALGORITHM = "HmacSHA256";

    public String getHmacHashCode(String data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(hmacKey.getBytes(), ALGORITHM);
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKeySpec);
            return bytesToHex(mac.doFinal(data.getBytes()));
        } catch (Exception exception) {
            throw new HMACException();
        }
    }

}
