package backend.Marine.units.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import backend.Marine.units.dto.WeatherDTO;
import backend.Marine.units.mapper.WeatherMapper;
import backend.Marine.units.model.weather.WeatherConditions;

@Service
public class WeatherDataFetcherServiceImpl implements WeatherDataFetcherService {
	private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
	private final String OPEN_WEATHER_API_KEY;
	private final RestTemplate REST_TEMPLATE = new RestTemplate();
	public WeatherDataFetcherServiceImpl(@Value("${weather.api.key}") String APIKey) {
		this.OPEN_WEATHER_API_KEY = APIKey;
	}

	@Override
	public WeatherDTO getWeatherForPosition(double lat, double lon) {
		WeatherConditions weather = callGetMethod(lat, lon);
		return WeatherMapper.INSTANCE.toWeatherDTO(weather);
	}

	private WeatherConditions callGetMethod(double lat, double lon) {

		String url = WEATHER_URL + "weather?lat={lat}&lon={lon}&appid=" + OPEN_WEATHER_API_KEY + "&units=metric&lang=en";
		ResponseEntity<WeatherConditions> response = REST_TEMPLATE.exchange(url, HttpMethod.GET,
				new HttpEntity<>(new HttpHeaders()), WeatherConditions.class, lat, lon);
		return response.getBody();
	}
}
