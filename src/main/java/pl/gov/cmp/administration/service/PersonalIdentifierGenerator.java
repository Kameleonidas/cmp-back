package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.cmp.administration.exception.PersonalIdentifierGenerationException;
import pl.gov.cmp.auth.repository.UserAccountRepository;

import java.security.SecureRandom;
import java.util.Random;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class PersonalIdentifierGenerator {

    private static final Random RANDOM = new SecureRandom();
    private static final int MIN_VALUE = 10000000;
    private static final int MAX_VALUE = 99999999;
    private static final int MAX_RETRY_COUNT = 1000;

    private final UserAccountRepository userAccountRepository;

    public String generate() {
        for(int i = 0; i < MAX_RETRY_COUNT; i++) {
            final var personalIdentifier = RANDOM.longs(1, MIN_VALUE, MAX_VALUE)
                    .boxed()
                    .collect(toList())
                    .stream()
                    .findFirst()
                    .map(String::valueOf)
                    .orElseThrow(() -> new IllegalStateException("Error while generate local id"));
            if(!userAccountRepository.existsByLocalId(personalIdentifier)) {
                return personalIdentifier;
            }
        }
        throw new PersonalIdentifierGenerationException();
    }
}
