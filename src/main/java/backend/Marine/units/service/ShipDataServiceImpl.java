package backend.Marine.units.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import backend.Marine.units.model.Area;
import backend.Marine.units.model.TrackShip;

@Service
public class ShipDataServiceImpl implements ShipDataService{
	private final RestTemplate restTemplate = new RestTemplate();
	private final String SHIP_API_URL = "https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions";
	private final ShipAuthorizationService shipAuthorizationService;
	private final ShipService shipService;
	@Autowired
	public ShipDataServiceImpl(ShipAuthorizationService shipAuthorizationService, ShipService shipService){
		this.shipAuthorizationService = shipAuthorizationService;
		this.shipService =shipService;

	}
	@Override
	public void fetchShip(Area area) {
		HttpHeaders httpHeaders = createHttpHeaders();
		ResponseEntity<TrackShip[]> exchange = restTemplate.exchange(createUrl(area),
				HttpMethod.GET, new HttpEntity(httpHeaders), TrackShip[].class);

		TrackShip[] tracks = exchange.getBody();
		shipService.trackShipParseToShips(tracks);
	}
	private HttpHeaders createHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + shipAuthorizationService.getAccessToken());
		return httpHeaders;
	}
	private String createUrl(Area area) {
		return SHIP_API_URL +
				"?Xmin=" + area.getFromX() +
				"&Xmax=" + area.getToX() +
				"&Ymin=" + area.getFromY() +
				"&Ymax=" + area.getToY();
	}
}
