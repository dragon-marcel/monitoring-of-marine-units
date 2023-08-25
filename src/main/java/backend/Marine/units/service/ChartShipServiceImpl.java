package backend.Marine.units.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import backend.Marine.units.entity.Ship;
import backend.Marine.units.model.ChartShip;
import backend.Marine.units.repository.ShipRepository;

@Service
public class ChartShipServiceImpl implements ChartShipService {
	@PersistenceContext
	private EntityManager em;
	private final ShipRepository shipRepository;

	private final int HOUR_RANGE = 5;

	public ChartShipServiceImpl(ShipRepository shipRepository) {
		this.shipRepository = shipRepository;
	}

	@Override
	public List<ChartShip> getDataShipTypeChart() {
		List<Ship> ships = shipRepository.findAllInArea();

		List<ChartShip> dataShipType = ships.stream()
				.collect(Collectors.groupingBy(s -> ((Ship) s).getType().getDescription(), Collectors.counting()))
				.entrySet().stream().map(item -> new ChartShip(item.getKey(), item.getValue()))
				.collect(Collectors.toList());

		return dataShipType;
	}

	@Override
	public Integer getCountShipOutArea() {
		List<Ship> ships = shipRepository.findAllOutArea();
		return ((Long) ships.stream().count()).intValue();
	}

	@Override
	public Integer getCountShipInArea() {
		List<Ship> ships = shipRepository.findAllInArea();
		return ((Long) ships.stream().count()).intValue();
	}

	@Override
	public Integer getCountShipTracked(String username) {
		List<Ship> ships = shipRepository.findTrackedShipsByUsername(username);
		return ((Long) ships.stream().count()).intValue();
	}

	@Override
	public Integer getCountShipMovable() {
		List<Ship> ships = shipRepository.findAllMovable();

		return ((Long) ships.stream().count()).intValue();

	}

	@Override
	public Integer getCountShipUnMovable() {
		List<Ship> ships = shipRepository.findAllUnMovable();
		return ((Long) ships.stream().count()).intValue();

	}

	@Override
	public List<ChartShip> getCountShipByHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -HOUR_RANGE);
		Date date = calendar.getTime();

		List<ChartShip> dataChart = new ArrayList<>();
		List<Object[]> result = em.createQuery(
				"select cast(hour(p.createDateTime) as string) ,count(*) from Ship s join s.track p where p.createDateTime >= :datetime  group by s.mmsi,year(p.createDateTime),month(p.createDateTime),day(p.createDateTime), cast(hour(p.createDateTime) as string) having count(*) > 0")
				.setParameter("datetime", date).getResultList();

		calendar.add(Calendar.HOUR, +HOUR_RANGE);

		List<ChartShip> dataShipType = result.stream().collect(Collectors.groupingBy(o -> o[0], Collectors.counting()))
				.entrySet().stream().map(item -> new ChartShip((String) item.getKey(), item.getValue()))
				.collect(Collectors.toList());

		for (int h = 0; h < HOUR_RANGE; h++) {
			if (h > 0)
				calendar.add(Calendar.HOUR, -1);

			boolean exist = false;
			String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
			if (hour.length() == 1)
				hour = "0" + hour;
			for (int a = 0; a < dataShipType.size(); a++) {
				ChartShip chart = dataShipType.get(a);
				if (chart.getLabel().equals(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)))) {
					chart.setLabel(hour);

					dataChart.add(chart);
					exist = true;
					break;
				}
			}
			if (!exist)
				dataChart.add(new ChartShip(hour, 0L));

		}
		Collections.reverse(dataChart);

		return dataChart;
	}
}
