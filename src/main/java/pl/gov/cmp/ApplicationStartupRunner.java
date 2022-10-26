package pl.gov.cmp;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.gov.cmp.gugik.service.CemeteriesAddressesFiller;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationStartupRunner implements ApplicationRunner {

    private final CemeteriesAddressesFiller cemeteriesAddressesFiller;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Startup application runner ...");

        CompletableFuture.runAsync(cemeteriesAddressesFiller::process);

        log.info("Application runner ends.");
    }
}
