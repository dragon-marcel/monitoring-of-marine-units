package backend.Marine.units.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledShipTaskManagerImpl implements ScheduledShipTaskManager {
    private final ShipDataService shipDataFetcherService;
    private final AreaManager areaManager;
    @Value("${scheduledShipEnabled}")
    private boolean scheduledShipEnabled;
    @Autowired
    public ScheduledShipTaskManagerImpl(ShipDataService shipDataFetcherService, AreaManager areaManager) {
        this.shipDataFetcherService = shipDataFetcherService;
        this.areaManager = areaManager;
        areaManager.setArea();
    }

    @Scheduled(fixedRate = 20000)
    public void fetchShipsFromArea() {
        if (isScheduledShipEnabled()) shipDataFetcherService.fetchShip(areaManager.getArea());
    }
    private boolean isScheduledShipEnabled(){
        return scheduledShipEnabled;
    }
}
