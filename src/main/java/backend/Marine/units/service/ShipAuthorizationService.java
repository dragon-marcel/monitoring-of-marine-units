package backend.Marine.units.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface ShipAuthorizationService {

    @Scheduled(initialDelay = 3_000_000, fixedDelay = 3_000_000)
    void manageAccessToken();

    String getAccessToken();
}
