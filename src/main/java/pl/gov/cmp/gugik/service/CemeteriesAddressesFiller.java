package pl.gov.cmp.gugik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.cemetery.service.CemeteryAddressesChecker;
import pl.gov.cmp.gugik.model.dto.AddressDto;
import pl.gov.cmp.gugik.model.entity.GugikCemeteryGeometryEntity;
import pl.gov.cmp.gugik.repository.GugikCemeteryGeometryRepository;
import pl.gov.cmp.process.service.ProcessService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static pl.gov.cmp.process.model.enums.ProcessCode.CEMETERIES_ADDRESSES_FILLER;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class CemeteriesAddressesFiller {

    private final GugikCemeteryGeometryRepository gugikCemeteryGeometryRepository;
    private final GugikAddressesFiller gugikAddressesFiller;
    private final ProcessService processService;
    private final CemeteryAddressesChecker cemeteryAddressesChecker;

    public void process() {
        log.debug("CemeteriesAddressesFiller processing ...");
        StopWatch timer = StopWatch.createStarted();
        // TODO przenieść szkielet wykonywania procesu do ProcessService
        if (ifProcessCouldBeRun()) {
            processFillAddresses();
        } else {
            logSkippingInformation();
        }
        timer.stop();
        log.info("CemeteriesAddressesFiller processed [time={} ms.]", timer.getTime());
    }

    private void logSkippingInformation() {
        var cemeteryWithoutAddressesCount = gugikCemeteryGeometryRepository
                .countCemeteriesWithoutAddresses();
        log.info("Skipping addresses fill [cemeteryWithoutAddresses={}].", cemeteryWithoutAddressesCount);
    }

    private void processFillAddresses() {
        try {
            fillAddresses();
            checkCemeteryAddresses();
            processService.markProcessAsWasRun(CEMETERIES_ADDRESSES_FILLER);
            log.info("Process marked as WAS_RUN.");
        } catch (RuntimeException runtimeException) {
            log.error("Exception during address fill, mark process as WAS_NOT_RUN", runtimeException);
            processService.markProcessAsWasNotRun(CEMETERIES_ADDRESSES_FILLER);
        }
    }

    private void checkCemeteryAddresses() {
        log.info("Checking addresses in cemeteries ...");
        cemeteryAddressesChecker.checkCemeteries();
    }

    private void fillAddresses() {
        log.info("Staring fill addresses in gugik_cemetery_geometries ...");

        var cemeteriesWithAddressToFill = gugikCemeteryGeometryRepository
                .findCemeteriesWithoutAddresses();
        log.info("Got entities with empty addresses [count={}]", cemeteriesWithAddressToFill.size());

        var counter = new AtomicLong();

        Map<GugikCemeteryGeometryEntity, AddressDto> cemeteryAddresses = cemeteriesWithAddressToFill
                .parallelStream()
                .map(cemetery -> {
                    log.info("Getting address for {}/{}  element ...", counter.incrementAndGet(), cemeteriesWithAddressToFill.size());
                    return Map.entry(cemetery, gugikAddressesFiller.processCemeteryGeometry(cemetery));
                })
                .filter(entry -> entry.getValue().isPresent())
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        counter.set(0L);
        cemeteryAddresses
                .forEach((key, value) -> {
                    log.info("Setting address for {}/{}  element ...", counter.incrementAndGet(), cemeteriesWithAddressToFill.size());
                    gugikAddressesFiller.processFetchedAddress(key, value);
                });

        log.info("Addresses filled.");
    }

    private boolean ifProcessCouldBeRun() {
        try {
            return processService.changeProcessToIsRunningState(CEMETERIES_ADDRESSES_FILLER);
        } catch (Exception exception) {
            // in case when two instances of application try to change process status (when
            // no process on DB)
            // in one instance there will be exception because we have unique index on code
            // column
            log.warn("Exception during changing process state to IsRunning: {}", exception.getMessage());
            return false;
        }
    }
}
