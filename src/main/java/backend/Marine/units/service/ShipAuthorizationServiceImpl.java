package backend.Marine.units.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class ShipAuthorizationServiceImpl implements ShipAuthorizationService{
    private final AtomicReference<String> apiClientId = new AtomicReference<>();
    private final AtomicReference<String> apiSecret = new AtomicReference<>();
    private final AtomicReference<String> accessToken = new AtomicReference<>();
    private final String AUTHENTICATION_URL = "https://id.barentswatch.no/connect/token";
    private final RestTemplate restTemplate = new RestTemplate();
    public ShipAuthorizationServiceImpl(@Value("${security.SHIP_client_id}") String clientId,
                                    @Value("${security.SHIP_client_secret}") String clientSecret, ShipServiceImpl shipService) throws Exception {

        if (clientId == null || clientSecret == null)
            throw new IllegalArgumentException("Missing SHIP secret or id in environment.");

        this.apiClientId.set(clientId);
        this.apiSecret.set(clientSecret);
        this.manageAccessToken();

    }
    @Scheduled(initialDelay = 3_000_000, fixedDelay = 3_000_000)
    public void manageAccessToken() {
        HttpHeaders httpHeaders = createHttpHeaders();
        MultiValueMap<String, String> body = createRequestBody();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<Map> response = sendAuthenticationRequest(entity);
        String token = extractTokenFromResponse(response);
        setAccessToken(token);
    }
    private MultiValueMap<String, String> createRequestBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", apiClientId.get());
        body.add("scope", "api");
        body.add("client_secret", apiSecret.get());
        body.add("grant_type", "client_credentials");
        return body;
    }
    private ResponseEntity<Map> sendAuthenticationRequest(HttpEntity<MultiValueMap<String, String>> entity) {
        return restTemplate.exchange(AUTHENTICATION_URL, HttpMethod.POST, entity, Map.class);
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
    private String extractTokenFromResponse(ResponseEntity<Map> response) {
        LinkedHashMap<String, String> responseBody = (LinkedHashMap<String, String>) response.getBody();
        String token = responseBody != null ? responseBody.get("access_token") : null;
        if (token == null) {
            throw new IllegalStateException("Ship Token is null");
        }
        return token;
    }

    private void setAccessToken(String token) {
        accessToken.set(token);
    }
    public String getAccessToken(){
        return accessToken.get();
    }
}
