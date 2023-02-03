package backend.Marine.units.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import backend.Marine.units.mapper.WeatherMapper;
import backend.Marine.units.model.weather.WeatherConditions;
import dto.WeatherDTO;

@Service
public class WeatherApiService {
	private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
	private final String OPENWEATHER_API_KEY;
	private RestTemplate restTemplate = new RestTemplate();

	public WeatherApiService(@Value("${weather.api.key}") String APIKey) {
		this.OPENWEATHER_API_KEY = APIKey;
	}

	public WeatherDTO getWeatherForPosition(double lat, double lon) {
		System.out.println("OPENWEATHER_API_KEY" + OPENWEATHER_API_KEY);
		WeatherConditions weather = callGetMethod(lat, lon);
		return WeatherMapper.INSTANCE.toWeatherDTO(weather);
	}

	private WeatherConditions callGetMethod(double lat, double lon) {

		String url = WEATHER_URL + "weather?lat={lat}&lon={lon}&appid=" + OPENWEATHER_API_KEY + "&units=metric&lang=en";
		ResponseEntity<WeatherConditions> response = restTemplate.exchange(url, HttpMethod.GET,
				new HttpEntity<>(new HttpHeaders()), WeatherConditions.class, lat, lon);
		return response.getBody();
	}
}
