package backend.Marine.units.service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import backend.Marine.units.model.Area;
import backend.Marine.units.model.TrackShip;

@Service
public class ShipDataFetcherServiceImpl {
	private final RestTemplate restTemplate = new RestTemplate();
	private final String SHIP_API_URL = "https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions";
	private final ShipAuthorizationService shipAuthorizationService;
	private final ShipService shipService;
	public ShipDataFetcherServiceImpl(ShipAuthorizationService shipAuthorizationService,ShipService shipService){
		this.shipAuthorizationService = shipAuthorizationService;
		this.shipService =shipService;

	}
	public void fetchShip(Area area) {
		HttpHeaders httpHeaders = createHttpHeaders();
		ResponseEntity<TrackShip[]> exchange = restTemplate.exchange(createUrl(area),
				HttpMethod.GET, new HttpEntity(httpHeaders), TrackShip[].class);

		TrackShip[] tracks = exchange.getBody();
		System.out.println("Refresh ships: " + tracks.length);

		shipService.trackShipParsetoShips(tracks);
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
