package backend.Marine.units.service;

import backend.Marine.units.model.Area;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

public class ScheduledShipTasksManagerTest {
    private ShipDataService shipDataFetcherService;
    private AreaManager areaManager;
    private ScheduledShipTaskManager scheduledShipTasksManager;

    @Before
    public void setUp() throws Exception {
        shipDataFetcherService = mock(ShipDataService.class);
        areaManager = mock(AreaManager.class);
        scheduledShipTasksManager = new ScheduledShipTaskManagerImpl(shipDataFetcherService, areaManager);
        Area area = new Area(10.09094, 63.3989, 10.67047, 63.58645);
        Mockito.when(areaManager.getArea()).thenReturn(area);
    }

    @Test
    public void should_fetch_ship_from_area_when_scheduledShipEnabled_is_true() {
        //given

        //when
        ReflectionTestUtils.setField(scheduledShipTasksManager, "scheduledShipEnabled", true);
        scheduledShipTasksManager.fetchShipsFromArea();

        //then
        verify(shipDataFetcherService, times(
                1)).fetchShip(areaManager.getArea());
    }
    @Test
    public void should_not_fetch_ship_when_scheduledShipEnabled_is_false(){
        //given

        //when
        ReflectionTestUtils.setField(scheduledShipTasksManager, "scheduledShipEnabled", false);
        scheduledShipTasksManager.fetchShipsFromArea();

        //then
        verify(shipDataFetcherService, never()).fetchShip(areaManager.getArea());
    }
}
