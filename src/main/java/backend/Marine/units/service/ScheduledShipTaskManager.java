package backend.Marine.units.service;

import org.springframework.scheduling.annotation.Scheduled;

interface ScheduledShipTaskManager {
    @Scheduled(fixedRate = 20000)
    public void fetchShipsFromArea();
}
