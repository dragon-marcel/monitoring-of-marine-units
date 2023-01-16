package backend.Marine.units.service;

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
	private static final String OPENWEATHER_API_KEY = "17aa55fa1293c0f4e5cd99167d1a0d71";

	public WeatherDTO getWeatherForPosition(double lat, double lon) {
		RestTemplate restTemplate = new RestTemplate();

		String url = WEATHER_URL + "weather?lat={lat}&lon={lon}&appid=" + OPENWEATHER_API_KEY + "&units=metric&lang=en";
		ResponseEntity<WeatherConditions> response = restTemplate.exchange(url, HttpMethod.GET,
				new HttpEntity<>(new HttpHeaders()), WeatherConditions.class, lat, lon);
		WeatherConditions weather = response.getBody();
		WeatherDTO watherDTO = WeatherMapper.INSTANCE.toWeatherDTO(weather);
		return watherDTO;
	}
}
