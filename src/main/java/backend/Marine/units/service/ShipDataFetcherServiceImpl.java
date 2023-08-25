package backend.Marine.units.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import backend.Marine.units.model.Area;
import backend.Marine.units.model.TrackShip;

@Service
@EnableScheduling
public class ShipDataFetcherServiceImpl implements ShipDataFetcherService {
	private final AtomicReference<String> apiClientId = new AtomicReference<>();
	private final AtomicReference<String> apiSecret = new AtomicReference<>();
	private final AtomicReference<String> accessToken = new AtomicReference<>();
	private final String AUTHENTICATION_URL = "https://id.barentswatch.no/connect/token";
	private final RestTemplate restTemplate = new RestTemplate();
	private final ShipServiceImpl shipService;
	private Area arena;

	public ShipDataFetcherServiceImpl(@Value("${security.SHIP_client_id}") String clientId,
			@Value("${security.SHIP_client_secret}") String clientSecret, ShipServiceImpl shipService) throws Exception {

		if (clientId == null || clientSecret == null)
			throw new IllegalArgumentException("Missing SHIP secret or id in environment.");

		this.apiClientId.set(clientId);
		this.apiSecret.set(clientSecret);
		this.shipService = shipService;
		this.setArena();
		this.getAccessToken();

	}

	@Scheduled(initialDelay = 3_000_000, fixedDelay = 3_000_000)
	@Override
	public void getAccessToken() {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", apiClientId.get());
		body.add("scope", "api");
		body.add("client_secret", apiSecret.get());
		body.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(body,
				httpHeaders);
		ResponseEntity<Map> response = restTemplate.exchange(AUTHENTICATION_URL, HttpMethod.POST, entity, Map.class);
		LinkedHashMap<String, String> responseBody = (LinkedHashMap<String, String>) response.getBody();
		String token = (String) responseBody.get("access_token");

		if (token == null) {
			throw new IllegalStateException("Ship Token is null");
		}

		this.accessToken.set(token);
	}

	@Scheduled(fixedRate = 20000)
	public void fetchShipsFromArea() {
		fetchShip(getArea());
	}

	@Override
	public void fetchShip(Area area) {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + accessToken.get());
		ResponseEntity<TrackShip[]> exchange = restTemplate.exchange(
				"https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?Xmin=" + getArea().getFromX() + "&Xmax="
						+ getArea().getToX() + "&Ymin=" + getArea().getFromY() + "&Ymax=" + getArea().getToY(),
				HttpMethod.GET, new HttpEntity(httpHeaders), TrackShip[].class);

		TrackShip[] tracks = exchange.getBody();
		System.out.println("Refresh ships: " + tracks.length);

		shipService.trackShipParsetoShips(tracks);
	}

	@Override
	public Area getArea() {
		return arena;
	}

	@Override
	public void setArena() {
		arena = new Area(10.09094, 63.3989, 10.67047, 63.58645);
	}
}
