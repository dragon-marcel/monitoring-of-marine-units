package backend.Marine.units.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.dto.WeatherDTO;
import backend.Marine.units.service.WeatherDataFetcherService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/weather")
public class WeatherController {
	private final WeatherDataFetcherService weatherApiService;

	public WeatherController(WeatherDataFetcherService weatherApiService) {
		this.weatherApiService = weatherApiService;
	}

	@GetMapping("/lat/{lat}/lon/{lon}")
	@ApiOperation("Get weather data by latitude and longitude")
	public ResponseEntity<WeatherDTO> getWeatherByLatLon(@PathVariable double lat, @PathVariable double lon) {
		WeatherDTO weather = weatherApiService.getWeatherForPosition(lat, lon);

		return new ResponseEntity<WeatherDTO>(weather, HttpStatus.OK);
	}

}
