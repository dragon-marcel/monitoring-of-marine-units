package backend.Marine.units.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class AuthorizationService {
    private final AtomicReference<String> apiClientId = new AtomicReference<>();
    private final AtomicReference<String> apiSecret = new AtomicReference<>();
    private final AtomicReference<String> accessToken = new AtomicReference<>();
    private final String AUTHENTICATION_URL = "https://id.barentswatch.no/connect/token";
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
}
